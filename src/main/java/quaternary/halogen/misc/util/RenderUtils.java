package quaternary.halogen.misc.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderUtils {
	
	/**
	 * Helper function to spawn some particles clientside, without dealing with packets or other junk
	 */
	public static void clientsideVanillaParticles(EnumParticleTypes type, Vec3d pos, double speed, int count, int... params) {
		//Offset the position a bit to *roughly* account for the XYZ position
		//passed in being the left side of the particle, not the center
		Vec3d offsetPos = pos.addVector(0.1, 0.1, 0.1);
		
		int id = type.getParticleID();
		
		for(int i = 0; i < count; i++) {
			//https://math.stackexchange.com/questions/44689/how-to-find-a-random-axis-or-unit-vector-in-3d
			double theta = Math.random() * Math.PI * 2;
			double z = Math.random() * 2 - 1;
			double coeff = Math.sqrt(1 - z * z);
			
			double randSpeed = Math.random() * speed;
			
			double x = coeff * Math.cos(theta) * randSpeed;
			double y = coeff * Math.sin(theta) * randSpeed;
			z *= randSpeed;
			
			Minecraft.getMinecraft().effectRenderer.spawnEffectParticle(id, offsetPos.x, offsetPos.y, offsetPos.z, x, y, z, params);
		}
	}
	
	/** Convenience function to call clientsideVanillaParticle with item crack particles */
	public static void clientsideItemCrackParticles(EntityItem ent, double speed, int amount) {
		//TODO 1.13: Item data value usage
		ItemStack stack = ent.getItem/*Stack*/();
		clientsideVanillaParticles(EnumParticleTypes.ITEM_CRACK, ent.getPositionVector(), speed, amount, Item.getIdFromItem(stack.getItem()), stack.getItemDamage());
	}
}
