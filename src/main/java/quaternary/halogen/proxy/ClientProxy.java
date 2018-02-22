package quaternary.halogen.proxy;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import quaternary.halogen.client.entityrenderer.RenderNothing;
import quaternary.halogen.entity.EntityRift;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerEntityRenderers() {
		//SOON TM
		RenderingRegistry.registerEntityRenderingHandler(EntityRift.class, RenderNothing::new);
	}
}
