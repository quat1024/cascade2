package quaternary.halogen.tile.node;
// Comment with space after the slashes to appease Nerxit.
//Hey nerxiepoo shut up about my comment habits k

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import quaternary.halogen.Halogen;
import quaternary.halogen.aura.type.AuraTypes;
import quaternary.halogen.cap.aura.IAuraEmitter;
import quaternary.halogen.cap.aura.IAuraReceiver;
import quaternary.halogen.cap.aura.IAuraStorage;
import quaternary.halogen.cap.aura.impl.AuraEmitterCap;
import quaternary.halogen.cap.aura.impl.AuraReceiverCap;
import quaternary.halogen.cap.aura.impl.AuraStorageCap;
import quaternary.halogen.item.ItemAuraCrystal;
import quaternary.halogen.util.DisgustingNumbers;
import quaternary.halogen.util.RenderUtils;
import quaternary.halogen.util.Utils;

import javax.annotation.Nullable;
import java.util.List;

public class TileNode extends TileEntity implements ITickable {
	private static final AxisAlignedBB DETECTION_AABB = new AxisAlignedBB(0.2,0.2,0.2,0.8,0.8,0.8);
	
	private final AuraStorageCap storageCap = new AuraStorageCap(DisgustingNumbers.NODE_MAX_AURA);
	private final AuraEmitterCap emitterCap = new AuraEmitterCap(storageCap);
	private final AuraReceiverCap receiverCap = new AuraReceiverCap(storageCap);
	
	private int auraAbsorptionCooldown = 0;
	
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
						auraAbsorptionCooldown = 20;
						
						if(world.isRemote) {
							Vec3d particlePos = ent.getPositionVector().addVector(.1,.1,.1);
							RenderUtils.clientsideParticle(EnumParticleTypes.ITEM_CRACK, particlePos, .3, 10, Item.getIdFromItem(stack.getItem()), stack.getItemDamage());
						}
						
						stack.shrink(1);
						break;
					}
				}
			}
		}
		
		//Temp af
		if(emitterCap.isEligible() && world.getTotalWorldTime() % 15 == 0) {
			for(EnumFacing whichWay : EnumFacing.values()) {
				for(int dist = 1; dist < 16; dist++) {
					TileEntity bepsi = world.getTileEntity(pos.offset(whichWay, dist));
					if(bepsi == null) continue;
					if(bepsi.hasCapability(RECEIVER, whichWay.getOpposite())) {
						IAuraReceiver otherCap = bepsi.getCapability(RECEIVER, whichWay.getOpposite());
						if(emitterCap.canEmitAura(AuraTypes.NORMAL, 10, otherCap)) {
							emitterCap.emitAura(AuraTypes.NORMAL, 10, otherCap);
						}
						
						break;
					}
				}
			}
		}
	}
	
	//Properties idk
	public int getComparatorLevel() {
		//todo temp
		return (int) Math.floor(storageCap.getTotalAura() / 1000d * 15);
	}	
	
	//Caps
	@CapabilityInject(IAuraStorage.class)
	public static Capability<IAuraStorage> STORAGE = null;
	
	@CapabilityInject(IAuraEmitter.class)
	public static Capability<IAuraEmitter> EMITTER = null;
	
	@CapabilityInject(IAuraReceiver.class)
	public static Capability<IAuraReceiver> RECEIVER = null;
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		if(capability == STORAGE || capability == RECEIVER) return true;
		if(capability == EMITTER) return facing != EnumFacing.UP;
		
		return super.hasCapability(capability, facing);
	}
	
	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
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
