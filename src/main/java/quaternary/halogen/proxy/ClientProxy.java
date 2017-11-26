package quaternary.halogen.proxy;

import quaternary.halogen.entity.HaloEntities;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerEntityRenderers() {
		HaloEntities.registerEntityRenderers();
	}
}
