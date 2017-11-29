package quaternary.halogen.tile.node;
// Comment with space after the slashes to appease Nerxit.
//Hey nerxiepoo shut up about my comment habits k

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import quaternary.halogen.aura.cap.*;
import quaternary.halogen.aura.cap.impl.*;
import quaternary.halogen.aura.type.AuraType;
import quaternary.halogen.aura.type.AuraTypes;
import quaternary.halogen.item.ItemAuraCrystal;
import quaternary.halogen.misc.DisgustingNumbers;
import quaternary.halogen.util.RenderUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class TileNode extends TileEntity implements ITickable {
	private static final AxisAlignedBB DETECTION_AABB = new AxisAlignedBB(0.2, 0.2, 0.2, 0.8, 0.8, 0.8);
	
	private AuraStorageCap storageCap;
	private AuraReceiverCap receiverCap;
	
	private HashMap<EnumFacing, AuraEmitterCap> emitters = new HashMap<>();
	
	private int auraAbsorptionCooldownTicks = 0;
	
	public TileNode() {
		storageCap = new AuraStorageCap(DisgustingNumbers.NODE_MAX_AURA);
		
		emitters.clear();
		for(EnumFacing whichWay : EnumFacing.values()) {
			if(whichWay == EnumFacing.UP) continue;
			if(whichWay == EnumFacing.DOWN) emitters.put(whichWay, new AuraEmitterCap(storageCap, true));
			else emitters.put(whichWay, new AuraEmitterCap(storageCap, false));
		}
		
		receiverCap = new AuraReceiverCap(storageCap);
	}
	
	@Override
	public void update() {
		if(auraAbsorptionCooldownTicks > 0) auraAbsorptionCooldownTicks--;
		
		if(auraAbsorptionCooldownTicks == 0) {
			List<EntityItem> nearbyItems = world.getEntitiesWithinAABB(EntityItem.class, DETECTION_AABB.offset(pos));
			nearbyItems.removeIf(itemEnt -> (itemEnt.getItem/*Stack*/().isEmpty() || 
			                               !(itemEnt.getItem/*Stack*/().getItem() instanceof ItemAuraCrystal)));
			for(EntityItem ent : nearbyItems) {
				ItemStack stack = ent.getItem/*Stack*/();
				
				//TODO: capabililties, not instanceof
				ItemAuraCrystal crystal = (ItemAuraCrystal) stack.getItem();
				
				AuraType type = crystal.type;
				int containedAura = crystal.getContainedAura(stack);
				
				if(storageCap.addAura(type, containedAura)) {
					auraAbsorptionCooldownTicks = 10;
					
					if(world.isRemote) {
						RenderUtils.clientsideVanillaParticles(EnumParticleTypes.ITEM_CRACK, ent.getPositionVector(), .3, 10, Item.getIdFromItem(stack.getItem()), stack.getItemDamage());
					}
					
					//update comparator level without causing a block update.
					world.updateComparatorOutputLevel(pos, world.getBlockState(pos).getBlock());
					
					stack.shrink(1);
					break;
				}
			}
		}
		
		//
		//            here lies exclamation point of doom
		//
		//
		//        _.---,._,'
		//   /' _.--.<
		//     /'     `'
		//   /' _.---._____
		//   \.'   ___, .-'`
		//       /'    \\                .
		//     /'       `-.             -|-
		//    |                          |
		//    |                   .----'~~~`----.
		//    |                 .'               `.
		//    |                 |     R  I  P     |
		//    |                 |                 |
		//    |                 | !world.isRemote |
		//     \              \\|                 |//
		//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
		//
		//
		//              oct 30, 2017 - oct 30, 2017
		//
		
		if(world.isRemote || world.getTotalWorldTime() % 15 != 0) return;
		
		for(Map.Entry<EnumFacing, AuraEmitterCap> entry : emitters.entrySet()) {
			EnumFacing whichWay = entry.getKey();
			AuraEmitterCap emitter = entry.getValue();
			
			if(emitter.isEligible()) {
				for(int dist = 1; dist < 16; dist++) {
					BlockPos toCheck = pos.offset(whichWay, dist);
					
					if(!world.isBlockLoaded(toCheck)) break;
					if(!canConnectionPass(world.getBlockState(toCheck))) break;
					
					TileEntity otherTE = world.getTileEntity(toCheck);
					if(otherTE == null) continue;
					
					if(otherTE.hasCapability(RECEIVER, whichWay.getOpposite())) {
						IAuraReceiver otherReceiver = otherTE.getCapability(RECEIVER, whichWay.getOpposite());
						assert otherReceiver != null;
						
						emitter.emitAura(AuraTypes.NORMAL, DisgustingNumbers.NODE_AURA_BURST_SIZE, otherReceiver);
						world.updateComparatorOutputLevel(pos, world.getBlockState(pos).getBlock());
						break; //don't emit to another object behind this one
					}
				}
			}
		}
		
		//Even temporarier than the previous temporary thing
		if(receiverCap.shouldUpdateComparator) {
			world.updateComparatorOutputLevel(pos, world.getBlockState(pos).getBlock());
			receiverCap.shouldUpdateComparator = false;
		}
	}
	
	//THINGS TO PUT IN ANOTHER CLASS EVENTUALLY LOLOL
	//REMEMBER HOW WELL THAT WORKED LAST TIME
	private boolean canConnectionPass(IBlockState state) {
		return (state.getMaterial().isReplaceable() || state.getMaterial().isLiquid()) || !state.isFullBlock() && !state.isFullCube();
	}
	
	//Properties idk
	public int getComparatorLevel() {
		//todo temp
		if(storageCap.hasAura()) {
			return MathHelper.floor(MathHelper.clampedLerp(1, 15, storageCap.getTotalAura() / (double) DisgustingNumbers.NODE_MAX_AURA));
		} else return 0;
	}
	
	//Caps
	@CapabilityInject(IAuraStorage.class)
	private static Capability<IAuraStorage> STORAGE = null;
	
	@CapabilityInject(IAuraEmitter.class)
	private static Capability<IAuraEmitter> EMITTER = null;
	
	@CapabilityInject(IAuraReceiver.class)
	private static Capability<IAuraReceiver> RECEIVER = null;
	
	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		if(capability == STORAGE || capability == RECEIVER) return true;
		if(capability == EMITTER) return facing != EnumFacing.UP;
		
		return super.hasCapability(capability, facing);
	}
	
	@Nullable
	@Override
	@SuppressWarnings("unchecked") //Everything is fine!!!!
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		if(capability == STORAGE) return (T) storageCap;
		if(capability == RECEIVER) return (T) receiverCap;
		if(capability == EMITTER && facing != EnumFacing.UP) return (T) emitters.get(facing);
		
		return super.getCapability(capability, facing);
	}
	
	//NBT
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setTag("Aura", storageCap.writeNBT());
		nbt.setInteger("AbsorptionCooldown", auraAbsorptionCooldownTicks);
		//todo: write emitters
		return super.writeToNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		storageCap.readNBT(nbt.getTagList("Aura", 10));
		auraAbsorptionCooldownTicks = nbt.getInteger("AbsorptionCooldown");
		//todo: read emitters
		super.readFromNBT(nbt);
	}
}
