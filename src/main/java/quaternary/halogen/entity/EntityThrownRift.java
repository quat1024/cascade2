package quaternary.halogen.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import quaternary.halogen.Halogen;

public class EntityThrownRift extends EntityThrowable {
	public EntityThrownRift(World w) {
		super(w);
	}
	
	public EntityThrownRift(World w, EntityLivingBase thrower) {
		super(w, thrower);
	}
	
	@Override
	protected void onImpact(RayTraceResult result) {
		if(world.isRemote) return;
		
		Halogen.LOGGER.info("bup");
		setDead();
	}
}