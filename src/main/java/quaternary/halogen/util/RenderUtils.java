package quaternary.halogen.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RenderUtils {
	
	/** Helper function to spawn some particles clientside, without dealing with packets or other junk */
	@SideOnly(Side.CLIENT)
	public static void clientsideParticle(EnumParticleTypes type, Vec3d pos, double speed, int count, int... params) {
		//Isn't it funny that you have to convert this type into a particle ID, where spawnEffectParticle
		//immediately converts it right back to an enumparticletypes?
		//Isn't that fabulous.
		int id = type.getParticleID();
		
		//Anyways.
		
		for(int i=0; i < count; i++) {
			//https://math.stackexchange.com/questions/44689/how-to-find-a-random-axis-or-unit-vector-in-3d
			double theta = Math.random() * Math.PI * 2;
			double z = Math.random() * 2 - 1;
			double coeff = Math.sqrt(1-z*z);
			
			double randSpeed = Math.random() * speed;
			
			double x = coeff * Math.cos(theta) * randSpeed;
			double y = coeff * Math.sin(theta) * randSpeed;
			z *= randSpeed;
			
			Minecraft.getMinecraft().effectRenderer.spawnEffectParticle(id, pos.x, pos.y, pos.z, x, y, z, params);
			//w.spawnParticle(type, pos.x, pos.y, pos.z, x, y, z, params);
		}
	}
}
