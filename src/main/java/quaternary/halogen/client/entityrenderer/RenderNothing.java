package quaternary.halogen.client.entityrenderer;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/** Dummy entity renderer for entities that are supposed to be invisible.
 * Essentially it's RenderAreaEffectCloud with a generic. */
@SideOnly(Side.CLIENT)
public class RenderNothing<T extends Entity> extends Render<T> {
	public RenderNothing(RenderManager manager) {
		super(manager);
	}
	
	@Nullable
	protected ResourceLocation getEntityTexture(T entity)
	{
		return null;
	}
}
