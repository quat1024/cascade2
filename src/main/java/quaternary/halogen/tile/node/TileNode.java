package quaternary.halogen.tile.node;
// Comment with space after the slashes to appease Nerxit.
//Hey nerxiepoo shut up about my comment habits k

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import quaternary.halogen.Halogen;
import quaternary.halogen.aura.type.AuraTypes;
import quaternary.halogen.cap.AuraStorageCap;
import quaternary.halogen.item.ItemAuraCrystal;
import quaternary.halogen.util.DisgustingNumbers;
import quaternary.halogen.util.RenderUtils;

import java.util.List;

public class TileNode extends TileEntity implements ITickable {
	private static final AxisAlignedBB DETECTION_AABB = new AxisAlignedBB(0.2,0.2,0.2,0.8,0.8,0.8);
	
	private final AuraStorageCap storageCap = new AuraStorageCap(DisgustingNumbers.NODE_MAX_AURA);
	int auraAbsorptionCooldown = 0;
	
	@Override
	public void update() {
		if(auraAbsorptionCooldown > 0) auraAbsorptionCooldown--;
		
		if(auraAbsorptionCooldown == 0) {
			List<EntityItem> nearbyItems = world.getEntitiesWithinAABB(EntityItem.class, DETECTION_AABB.offset(pos));
			nearbyItems.removeIf(itemEnt -> itemEnt.getItem/*Stack*/().isEmpty());
			nearbyItems.removeIf(itemEnt -> !(itemEnt.getItem/*Stack*/().getItem() instanceof ItemAuraCrystal));
			if(!nearbyItems.isEmpty()) {
				for(EntityItem ent : nearbyItems) {
					ItemStack stack = ent.getItem/*Stack*/();
					//Blah blah blah ask the stack for its contained aura blah blah blah
					if(storageCap.canAddAura(AuraTypes.NORMAL, 50)) {
						storageCap.addAura(AuraTypes.NORMAL, 50, false);
						auraAbsorptionCooldown = 20;
						
						Halogen.LOGGER.info("Hi i'm a " + (world.isRemote ? "client" : "server") + " and I think there's " + storageCap.getAura(AuraTypes.NORMAL) + " aura in the thingie");
						
						//clientside only
						if(world.isRemote) {
							Vec3d particlePos = ent.getPositionVector().addVector(.1,.1,.1);
							Halogen.LOGGER.info("Bepis");
							RenderUtils.clientsideParticle(EnumParticleTypes.ITEM_CRACK, particlePos, .3, 10, Item.getIdFromItem(stack.getItem()), stack.getItemDamage());
						}
						
						/*
						//TODO: sound effect?
						if(world instanceof WorldServer) {
							WorldServer bepsi = (WorldServer) world;
							Vec3d particlePos = ent.getPositionVector().addVector(.1,.1,.1);
							Halogen.LOGGER.info("Item id " + Item.getIdFromItem(stack.getItem()));
							bepsi.spawnParticle(EnumParticleTypes.ITEM_CRACK, false, particlePos.x, particlePos.y, particlePos.z, 10, 0, 0, 0, 0.05d, Item.getIdFromItem(stack.getItem()));
						}
						*/
						
						stack.shrink(1);
						break;
					}
				}
			}
		}
	}
	
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
