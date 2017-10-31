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
import quaternary.halogen.aura.type.AuraTypes;
import quaternary.halogen.item.ItemAuraCrystal;
import quaternary.halogen.misc.DisgustingNumbers;
import quaternary.halogen.util.RenderUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static quaternary.halogen.misc.DisgustingNumbers.NODE_AURA_BURST_SIZE;

public class TileNode extends TileEntity implements ITickable {
	private static final AxisAlignedBB DETECTION_AABB = new AxisAlignedBB(0.2, 0.2, 0.2, 0.8, 0.8, 0.8);
	
	private AuraStorageCap storageCap;
	private AuraEmitterCap emitterCap;
	private AuraReceiverCap receiverCap;
	
	private int auraAbsorptionCooldown = 0;
	
	public TileNode() {
		storageCap = new AuraStorageCap(DisgustingNumbers.NODE_MAX_AURA);
		emitterCap = new AuraEmitterCap(storageCap);
		receiverCap = new AuraReceiverCap(storageCap);
	}
	
	@Override
	public void update() {
		if(auraAbsorptionCooldown > 0) auraAbsorptionCooldown--;
		
		if(auraAbsorptionCooldown == 0) {
			List<EntityItem> nearbyItems = world.getEntitiesWithinAABB(EntityItem.class, DETECTION_AABB.offset(pos));
			nearbyItems.removeIf(itemEnt -> itemEnt.getItem/*Stack*/().isEmpty());
			//todo: capa🅱ilities instead of instanceof 
			nearbyItems.removeIf(itemEnt -> !(itemEnt.getItem/*Stack*/().getItem() instanceof ItemAuraCrystal));
			if(!nearbyItems.isEmpty()) {
				for(EntityItem ent : nearbyItems) {
					ItemStack stack = ent.getItem/*Stack*/();
					
					//Blah blah blah ask the stack for its contained aura blah blah blah
					int containedAura = DisgustingNumbers.AURA_CRYSTAL_CONTAINED_AURA;
					
					if(storageCap.addAura(AuraTypes.NORMAL, containedAura)) {
						auraAbsorptionCooldown = 10;
						
						if(world.isRemote) {
							Vec3d particlePos = ent.getPositionVector().addVector(.1, .1, .1);
							RenderUtils.clientsideParticle(EnumParticleTypes.ITEM_CRACK, particlePos, .3, 10, Item.getIdFromItem(stack.getItem()), stack.getItemDamage());
						}
						
						world.updateComparatorOutputLevel(pos, world.getBlockState(pos).getBlock());
						
						stack.shrink(1);
						break;
					}
				}
			}
		}
		
		//Temp af
		//put this in another class eventually
		if(!world.isRemote && emitterCap.isEligible() && world.getTotalWorldTime() % 15 == 0) {
			for(EnumFacing whichWay : EnumFacing.values()) {
				if(whichWay == EnumFacing.UP) continue; //quality code
				for(int dist = 1; dist < 16; dist++) {
					BlockPos toCheck = pos.offset(whichWay, dist);
					
					if(!world.isBlockLoaded(toCheck)) continue;
					if(!canConnectionPass(world.getBlockState(toCheck))) break;
					
					TileEntity bepsi = world.getTileEntity(toCheck);
					if(bepsi == null) continue;
					
					if(bepsi.hasCapability(RECEIVER, whichWay.getOpposite())) {
						IAuraReceiver otherCap = bepsi.getCapability(RECEIVER, whichWay.getOpposite());
						if(otherCap == null) continue; //Probably won't happen unless hasCapability is lying.
						
						emitterCap.emitAura(AuraTypes.NORMAL, NODE_AURA_BURST_SIZE, whichWay, otherCap);
						world.updateComparatorOutputLevel(pos, world.getBlockState(pos).getBlock());
						break;
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
		return(state.getMaterial().isReplaceable() || state.getMaterial().isLiquid()) || !state.isFullBlock() && !state.isFullCube();
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
		if(capability == EMITTER && facing != EnumFacing.UP) return (T) emitterCap;
		
		return super.getCapability(capability, facing);
	}
	
	//NBT
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setTag("Aura", storageCap.writeNBT());
		nbt.setInteger("AbsorptionCooldown", auraAbsorptionCooldown);
		return super.writeToNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		storageCap.readNBT(nbt.getTagList("Aura", 10));
		auraAbsorptionCooldown = nbt.getInteger("AbsorptionCooldown");
		super.readFromNBT(nbt);
	}
}
