package quaternary.cascade2.net.util;

import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;

public class CascadePacketUtils {
	//La la la don't mind me just stealing botania
	//(is this the best way to do packets???)
	public static void dispatchTEToNearby(TileEntity te) {
		if(te.getWorld().isRemote) return; //You friccin moron this only makes sense serverside
		
		SPacketUpdateTileEntity packet = te.getUpdatePacket();
		
		if(packet != null) {
			PlayerChunkMapEntry entry = ((WorldServer) te.getWorld()).getPlayerChunkMap().getEntry(te.getPos().getX() >> 4, te.getPos().getZ() >> 4);
			if(entry != null) entry.sendPacket(packet);
		}
	}
}
