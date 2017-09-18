package quaternary.cascade2.tile.node;
// Comment with space after the slashes to appease Nerxit.

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import quaternary.cascade2.aura.AuraContainerNotCapabilityDoNotUseSeriously;
import quaternary.cascade2.tile.CascadeTileEntity;
import quaternary.cascade2.util.CascadeUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TileEntityAuraNode extends CascadeTileEntity implements ITickable {
	
	private static final int CONNECTION_RANGE = 16;
	private static final AxisAlignedBB ITEM_DETECTION_AABB = new AxisAlignedBB(0, 0, 0, 1, 1, 1);
	
	private AuraContainerNotCapabilityDoNotUseSeriously auraContainer = new AuraContainerNotCapabilityDoNotUseSeriously(1000);
	private byte absorptionCooldown = 0;
	
	//This is a thing I could put in the cap
	private ConcurrentHashMap<EnumFacing, ConnectionData> connections = new ConcurrentHashMap<>();
	
	public void update() {
		
	}
	
	private void scanForConnections() {
		if(world.isRemote) return;
		
		for(EnumFacing whichWay : EnumFacing.values()) {
			boolean unblocked = true;
			for(int dist = 1; dist < CONNECTION_RANGE; dist++) {
				BlockPos otherPos = pos.offset(whichWay, dist);
				
				if(canConnectionPassThrough(world.getBlockState(otherPos))) unblocked = false;
				
				TileEntity checkedTE = world.getTileEntity(otherPos);
				if(checkedTE != null && checkedTE instanceof TileEntityAuraNode) {
					this.setConnection(whichWay, otherPos, unblocked);
					((TileEntityAuraNode) checkedTE).setConnection(whichWay.getOpposite(), this.pos, unblocked);
					break;
				}
			}
		}
	}
	
	//Helpers which should go in the capability
	private boolean canConnectionPassThrough(IBlockState state) {
		return state.getMaterial().isReplaceable() || state.getMaterial().isLiquid() && 
						!(state.isOpaqueCube() || state.isFullCube() || state.isFullBlock());
	}
	
	private void resetAllConnections() {
		connections.clear();
	}
	
	private void setConnection(EnumFacing key, BlockPos otherPos, boolean unblocked) {
		ConnectionData toAdd = new ConnectionData(otherPos, unblocked);
		if(toAdd.equals(connections.get(key))) return;
		
		connections.put(key, new ConnectionData(otherPos, unblocked));
	}
	
	private ConcurrentHashMap<EnumFacing, ConnectionData> getConnections() {
		return connections;
	}
	
	//todo: dirty flag this maybe?
	//because it will only ever update when the connection map does, if then
	public ConcurrentHashMap<EnumFacing, ConnectionData> getActiveConnections() {
		//TIMES LIKE THIS ARE WHERE I WISH I HAD SUPER AWESOME FUNCTIONAL LANG POWERS
		//BUT OH WELL GUESS I'LL JUST FORLOOP
		//BECAUSE IT'S LITERALLY BETTER THAN JAVA'S SHITTY STREAM BULL SHIT
		//S I G H
		ConcurrentHashMap<EnumFacing, ConnectionData> newConnections = new ConcurrentHashMap<>();
		for(Map.Entry<EnumFacing, ConnectionData> pair : connections.entrySet()) {
			if(pair.getValue().unblocked) {
				newConnections.put(pair.getKey(), pair.getValue());
			}
		}
		return newConnections;
	}
	
	//Events called from blockauranode
	public void onActivated(World w, EntityPlayer player) {
		//CascadeUtils.sendChatMessage(player, world.isRemote ? "Hi i'm the client" : "I'm the server");
		
		if(world.isRemote) CascadeUtils.sendChatMessage(player, "My name Jeff!!!!! xD");
		for(Map.Entry<EnumFacing, ConnectionData> pair : connections.entrySet()) {
			CascadeUtils.sendChatMessage(player,
							(world.isRemote ? "C " + TextFormatting.GREEN : "S " + TextFormatting.AQUA) +
							"Side: " + pair.getKey() + " " + pair.getValue().toNiceString());
		}
	}
	
	public void onPlaceBlock() {
		scanForConnections();
	}
	
	public void onBreakBlock() {
		world.removeTileEntity(pos);
		
		for(Map.Entry<EnumFacing, ConnectionData> pair : connections.entrySet()) {
			TileEntityAuraNode other = (TileEntityAuraNode) world.getTileEntity(pos);
			if(other == null) continue;
			other.resetAllConnections();
			other.scanForConnections();
		}
	}
	
	//NBT save load junk
	private static final String AURA_KEY = "Aura";
	private static final String ABSORPTION_COOLDOWN_KEY = "AuraCooldown";
	private static final String CONNECTIONS_LIST_KEY = "Connections";
	private static final String CONNECTION_DATA_KEY = "Data";
	private static final String FACING_KEY = "Facing";
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setTag(AURA_KEY, auraContainer.writeToNBTList());
		nbt.setByte(ABSORPTION_COOLDOWN_KEY, absorptionCooldown);
		NBTTagList nbtlist = new NBTTagList();
		for(Map.Entry<EnumFacing, ConnectionData> pair : connections.entrySet()) {
			EnumFacing whichWay = pair.getKey();
			ConnectionData data = pair.getValue();
			
			NBTTagCompound item = new NBTTagCompound();
			item.setInteger(FACING_KEY, whichWay.getIndex());
			item.setTag(CONNECTION_DATA_KEY, data.toNBTCompound());
			
			nbtlist.appendTag(item);
		}
		nbt.setTag(CONNECTIONS_LIST_KEY, nbtlist);
		
		return super.writeToNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		auraContainer.readFromNBTList(nbt.getTagList(AURA_KEY, 10)); //The 10 is some magic number idk.
		absorptionCooldown = nbt.getByte(ABSORPTION_COOLDOWN_KEY);
		
		connections.clear();
		NBTTagList conlist = nbt.getTagList(CONNECTIONS_LIST_KEY, 10);
		for(NBTBase e : conlist) {
			NBTTagCompound entry = (NBTTagCompound) e;
			
			EnumFacing whichWay = EnumFacing.values()[entry.getInteger(FACING_KEY)];
			ConnectionData data = new ConnectionData(entry);
			
			//todo: should i go through an "official channel" or just write directly?
			//I think this is better, honestly: the getters and setters made more sense
			//when I was using more than just a very simple k/v hashmap
			//But when a getter is just a wrapper for the hashmap's getter...? no.
			connections.put(whichWay, data);
		}
		
		super.readFromNBT(nbt);
	}
	
	public class ConnectionData {
		public BlockPos position;
		public boolean unblocked;
		
		public ConnectionData(BlockPos position_, boolean unblocked_) {
			position = position_;
			unblocked = unblocked_;
		}
		
		public ConnectionData(NBTTagCompound fromNBT) {
			position = new BlockPos(
							fromNBT.getInteger("x"),
							fromNBT.getInteger("y"),
							fromNBT.getInteger("z")
			);
			unblocked = fromNBT.getBoolean("Unblocked");
		}
		
		public NBTTagCompound toNBTCompound() {
			NBTTagCompound blah = new NBTTagCompound();
			blah.setInteger("x", position.getX());
			blah.setInteger("y", position.getY());
			blah.setInteger("z", position.getZ());
			blah.setBoolean("Unblocked", unblocked);
			return blah;
		}
		
		String toNiceString() {
			return "Pos: " + position.getX() + " " + position.getY() + " " + position.getZ() + " " + (unblocked ? " Unblocked" : " Blocked");
		}
		
		boolean equals(ConnectionData other) {
			if(other == null) return false;
			return position.equals(other.position) && (unblocked == other.unblocked);
		}
	}
}
