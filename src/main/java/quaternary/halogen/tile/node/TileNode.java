package quaternary.halogen.tile.node;
// Comment with space after the slashes to appease Nerxit.
//Hey nerxiepoo shut up about my comment habits k

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import quaternary.halogen.aura.cap.*;
import quaternary.halogen.aura.cap.impl.*;
import quaternary.halogen.aura.type.AuraType;
import quaternary.halogen.item.ItemAuraCrystal;
import quaternary.halogen.misc.DisgustingNumbers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.List;

public class TileNode extends TileEntity implements ITickable {
	private static final AxisAlignedBB DETECTION_AABB = new AxisAlignedBB(0.2, 0.2, 0.2, 0.8, 0.8, 0.8);
	
	private AuraStorageCap storageCap;
	private AuraEmitterCap emitterCap;
	private AuraReceiverCap receiverCap;
	
	private int auraAbsorptionCooldownTicks = 0;
	
	public TileNode() {
		storageCap = new AuraStorageCap(DisgustingNumbers.NODE_MAX_AURA);
		emitterCap = new AuraEmitterCap(storageCap);
		receiverCap = new AuraReceiverCap(storageCap);
	}
	
	@Override
	public void update() {
		if(auraAbsorptionCooldownTicks > 0) auraAbsorptionCooldownTicks--;
		
		//This code intentionally runs both client- and server-side.
		//Main reason being, the visually "stacked" items in the item entity
		//do not update on the client if I only change the server stack size.
		//Todo: This is far from ideal. Can I send a packet?
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
				
				//todo adding aura
			}
		}
		
		if(world.isRemote || world.getTotalWorldTime() % 15 != 0) return;
		
		//build a map of nearby aura nodes
		//Key: the direction the other node is in, compared to this one.
		//Value: the IAuraReceiver of that node (retrieved from the direction to it)
		//TODO cache this map
		EnumMap<EnumFacing, IAuraReceiver> nearbyReceivers = new EnumMap<>(EnumFacing.class);
		
		for(EnumFacing whichWay : EnumFacing.values()) {
			for(int dist = 1; dist < 16; dist++) {
				BlockPos posToCheck = pos.offset(whichWay, dist);
				
				//try a different direction if this position is not loaded.
				if(!world.isBlockLoaded(posToCheck)) break;
				
				//can this block hold aura?
				TileEntity tile = world.getTileEntity(posToCheck);
				if(tile != null && tile.hasCapability(RECEIVER, whichWay.getOpposite())) {
					IAuraReceiver otherRec = tile.getCapability(RECEIVER, whichWay.getOpposite());
					nearbyReceivers.put(whichWay, otherRec);
					break; //Check a different direction.
				}
				
				//try a different direction if this way is blocked.
				//(i check this after checking whether it's an aura block so i am able
				//to send aura to types of blocks that would otherwise block this ray)
				IBlockState state = world.getBlockState(posToCheck);
				if(!canConnectionPass(state)) break;
			}
		}
		
		//todo EMOT AURE
		emitterCap.emitAuraTick(DisgustingNumbers.NODE_AURA_BURST_SIZE, nearbyReceivers.clone());
		//nearbyReceivers.clone() is perfect to use here. I get a copy of the map's *structure*,
		//but all the elements inside the map are references to the real-world IAuraReceivers.
		//I can just emit aura to the IAuraReceivers inside the map and it will happen in-world.
	}
	
	//THINGS TO PUT IN ANOTHER CLASS EVENTUALLY LOLOL
	//REMEMBER HOW WELL THAT WORKED LAST TIME
	private boolean canConnectionPass(IBlockState state) {
		return (state.getMaterial().isReplaceable() || state.getMaterial().isLiquid()) || (!state.isFullBlock() && !state.isFullCube());
	}
	
	/*
	public int getComparatorLevel() {
		//todo temp
		if(storageCap.hasAura()) {
			return MathHelper.floor(MathHelper.clampedLerp(1, 15, storageCap.getTotalAura() / (double) DisgustingNumbers.NODE_MAX_AURA));
		} else return 0;
	}
	*/
	
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
		if(capability == EMITTER) return facing != null && facing != EnumFacing.UP;
		
		return super.hasCapability(capability, facing);
	}
	
	@Nullable
	@Override
	@SuppressWarnings("unchecked") //Everything is fine!!!!
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		if(capability == STORAGE) return (T) storageCap;
		if(capability == RECEIVER) return (T) receiverCap;
		if(capability == EMITTER && facing != EnumFacing.UP) return (T) emitterCap;
		if(facing == null) return (T) storageCap;
		
		return super.getCapability(capability, facing);
	}
	
	//NBT
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setTag("Emitter", emitterCap.writeNBT());
		nbt.setTag("Storage", storageCap.writeNBT());
		nbt.setTag("Receiver", receiverCap.writeNBT());
		
		nbt.setInteger("AbsorptionCooldown", auraAbsorptionCooldownTicks);
		return super.writeToNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		/*emitterCap.readNBT(nbt.getCompoundTag("Emitter"));
		storageCap.readNBT(nbt.getCompoundTag("Storage"));
		receiverCap.readNBT(nbt.getCompoundTag("Receiver"));*/
		
		auraAbsorptionCooldownTicks = nbt.getInteger("AbsorptionCooldown");
		super.readFromNBT(nbt);
	}
}
