package quaternary.cascade2.cap;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import quaternary.cascade2.aura.handler.IAuraContainer;

public class CascadeCapabilities {
	@CapabilityInject(IAuraContainer.class)
	public static Capability<IAuraContainer> CAPABILITY_CONTAINER = null;
	
	public static void registerCapabilities() {
		
	}
}
