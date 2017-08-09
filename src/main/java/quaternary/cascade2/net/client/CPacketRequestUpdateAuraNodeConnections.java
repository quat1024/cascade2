package quaternary.cascade2.net.client;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import quaternary.cascade2.Cascade;
import quaternary.cascade2.net.CascadeMessage;
import quaternary.cascade2.net.server.SPacketUpdateAuraNodeConnections;
import quaternary.cascade2.tile.node.TileEntityAuraNode;

public class CPacketRequestUpdateAuraNodeConnections extends CascadeMessage {
	
	private BlockPos pos;
	private int dimension = -5;
	
	//forge needs an empty one
	public CPacketRequestUpdateAuraNodeConnections() {}
	
	public CPacketRequestUpdateAuraNodeConnections(TileEntityAuraNode te) {
		this(te.getPos(), te.getWorld().provider.getDimension());
	}
	
	public CPacketRequestUpdateAuraNodeConnections(BlockPos pos_, int dimension_) {
		pos = pos_;
		dimension = dimension_;
	}
	
	public Side getSide() {
		return Side.CLIENT;
	}
	
	public IMessageHandler getHandler() {
		return new Handler();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		Cascade.LOGGER.info("Writing request for " + pos.toString() + " dimension " + dimension);
		buf.writeLong(pos.toLong());
		buf.writeInt(dimension);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = BlockPos.fromLong(buf.readLong());
		dimension = buf.readInt();
	}
	
	public static class Handler implements IMessageHandler<CPacketRequestUpdateAuraNodeConnections, SPacketUpdateAuraNodeConnections> {
		@Override
		public SPacketUpdateAuraNodeConnections onMessage(CPacketRequestUpdateAuraNodeConnections packet, MessageContext ctx) {
			Cascade.LOGGER.info("Handling request for " + packet.pos.toString() + " dimension " + packet.dimension);
			World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(packet.dimension);
			TileEntityAuraNode te = (TileEntityAuraNode) world.getTileEntity(packet.pos);
			Cascade.LOGGER.info(te);
			Cascade.LOGGER.info(te.getPos());
			if(te == null) return null;
			else return new SPacketUpdateAuraNodeConnections(te);
		}
	}
	
}
