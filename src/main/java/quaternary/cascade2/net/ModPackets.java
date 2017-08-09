package quaternary.cascade2.net;

import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import quaternary.cascade2.net.client.CPacketRequestUpdateAuraNodeConnections;
import quaternary.cascade2.net.server.SPacketUpdateAuraNodeConnections;

public class ModPackets {
	public static void registerPackets(SimpleNetworkWrapper nethandler) {
		registerPacket(nethandler, new SPacketUpdateAuraNodeConnections());
		registerPacket(nethandler, new CPacketRequestUpdateAuraNodeConnections());
	}
	
	private static int id = 0;
	public static void registerPacket(SimpleNetworkWrapper nethandler, CascadeMessage message) {
		nethandler.registerMessage(message.getHandler(), message.getClass(), id, Side.CLIENT);
	}
}
