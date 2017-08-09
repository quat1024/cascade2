package quaternary.cascade2.net.server;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import quaternary.cascade2.Cascade;
import quaternary.cascade2.net.CascadeMessage;
import quaternary.cascade2.tile.node.TileEntityAuraNode;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SPacketUpdateAuraNodeConnections extends CascadeMessage {
	
	private BlockPos pos;
	private ConcurrentHashMap<EnumFacing, BlockPos> connections;
	
	//empty one for Forge
	public SPacketUpdateAuraNodeConnections() {}
	
	public SPacketUpdateAuraNodeConnections(TileEntityAuraNode te) {
		this(te.getPos(),te.getConnectionMap());
	}
	
	public SPacketUpdateAuraNodeConnections(BlockPos pos_, ConcurrentHashMap<EnumFacing, BlockPos> connections_) {
		pos = pos_;
		connections = connections_;
	}
	
	public Side getSide() {
		return Side.SERVER;
	}
	
	public IMessageHandler getHandler() {
		return new Handler();
	}
	
	//long - Blockpos of the node in question
	//byte - Connection count
	//For each connection
	//  byte - EnumFacing connection direction
	//  long - Blockpos of the node at the connection target
	
	@Override
	public void toBytes(ByteBuf buf) {
		Cascade.LOGGER.info("Writing response for " + pos.toString());
		buf.writeLong(pos.toLong());
		buf.writeByte(connections.size());
		if(connections.size() > 0) {
			for(Map.Entry<EnumFacing, BlockPos> pair : connections.entrySet()) {
				EnumFacing whichWay = pair.getKey();
				BlockPos bp = pair.getValue();
				buf.writeByte(whichWay.getIndex());
				buf.writeLong(bp.toLong());
			}
		}
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = BlockPos.fromLong(buf.readLong());
		byte count = buf.readByte();
		connections.clear();
		for(int i=0; i < count; i++) {
			connections.put(
							EnumFacing.values()[buf.readByte()],
							BlockPos.fromLong(buf.readLong())
			);
		}
	}
	
	//This runs on the client
	public static class Handler implements IMessageHandler<SPacketUpdateAuraNodeConnections, IMessage> {
		@Override
		public IMessage onMessage(SPacketUpdateAuraNodeConnections packet, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				World world = Minecraft.getMinecraft().world;
				TileEntityAuraNode te = (TileEntityAuraNode) world.getTileEntity(packet.pos);
				te.replaceConnectionMap(packet.connections);
			});
			return null; //Don't fire back a packet
		}
	}
}
