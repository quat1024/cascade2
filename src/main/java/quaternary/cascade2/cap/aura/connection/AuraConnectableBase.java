package quaternary.cascade2.cap.aura.connection;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import quaternary.cascade2.Cascade;
import quaternary.cascade2.cap.CascadeCaps;
import quaternary.cascade2.util.CascadeUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AuraConnectableBase implements IAuraConnectable {
	private ConcurrentHashMap<EnumFacing, ConnectionData> connections = new ConcurrentHashMap<>();
	
	@Override
	public ConcurrentHashMap<EnumFacing, ConnectionData> getConnectionMap() {
		return connections;
	}
	
	@Override
	public void replaceConnectionMap(ConcurrentHashMap<EnumFacing, ConnectionData> newMap) {
		connections = newMap;
	}
	
	@Override
	public void eraseConnections() {
		connections.clear();
	}
	
	@Override
	public void rescanForConnections(World world, BlockPos pos) {
		//if(world.isRemote) return;
		
		connections.clear();
		
		for(EnumFacing whichWay : EnumFacing.values()) {
			boolean unblocked = true;
			for(int dist = 1; dist < CONNECTION_RANGE; dist++) {
				BlockPos otherPos = pos.offset(whichWay, dist);
				
				//if(canConnectionPassThrough(world.getBlockState(otherPos))) unblocked = false;
				
				TileEntity checkedTE = world.getTileEntity(otherPos);
				if(checkedTE == null) continue;
				if(!checkedTE.hasCapability(CascadeCaps.AURA_CONNECTABLE, whichWay.getOpposite())) continue;
				
				//good, looks like we found a tile entity with the capability! let's connect to it.
				IAuraConnectable hat = checkedTE.getCapability(CascadeCaps.AURA_CONNECTABLE, whichWay.getOpposite());
				hat.setConnection(whichWay.getOpposite(), new ConnectionData(pos, unblocked));
				this.setConnection(whichWay, new ConnectionData(otherPos, unblocked));
				break; //Move to the next direction
			}
		}
	}
	
	//todo: find a better place for this
	private boolean canConnectionPassThrough(IBlockState state) {
		return state.getMaterial().isReplaceable() || state.getMaterial().isLiquid() &&
						!(state.isOpaqueCube() || state.isFullCube() || state.isFullBlock());
	}
	//and this
	private static final int CONNECTION_RANGE = 16;
	
	@Override
	public void setConnection(EnumFacing key, ConnectionData value) {
		if(value.equals(connections.getOrDefault(key, null))) return;
		
		connections.put(key, value);
		//mark dirty?
	}
	
	@Override
	public void onRemoved(World world, BlockPos pos) {
		for(Map.Entry<EnumFacing, ConnectionData> pair : connections.entrySet()) {
			BlockPos otherPos = pair.getValue().position;
			TileEntity blah = world.getTileEntity(otherPos);
			if(blah == null) continue;
			if(!blah.hasCapability(CascadeCaps.AURA_CONNECTABLE, pair.getKey().getOpposite())) continue;
			blah.getCapability(CascadeCaps.AURA_CONNECTABLE, pair.getKey().getOpposite()).rescanForConnections(world, otherPos);
		}
	}
	
	@Override
	public void lalalaDebugPrintOwoWhatsThis(World w, EntityPlayer p) {
		for(Map.Entry<EnumFacing, ConnectionData> pair : getConnectionMap().entrySet()) {
			CascadeUtils.sendChatMessage(p,
							(w.isRemote ? "C " + TextFormatting.GREEN : "S " + TextFormatting.AQUA) +
											"Side: " + pair.getKey() + " " + pair.getValue().toNiceString());
		}
	}
	
	
	@Override
	public NBTBase writeNBT() {
		NBTTagList nbtlist = new NBTTagList();
		for(Map.Entry<EnumFacing, ConnectionData> pair : connections.entrySet()) {
			EnumFacing whichWay = pair.getKey();
			ConnectionData data = pair.getValue();
			
			NBTTagCompound item = new NBTTagCompound();
			item.setInteger("facing", whichWay.getIndex());
			item.setTag("data", data.toNBTCompound());
			
			nbtlist.appendTag(item);
		}
		return nbtlist;
	}
	
	@Override
	public void readNBT(NBTBase nbt) {
		if(!(nbt instanceof NBTTagList)) throw new IllegalArgumentException("Tried to decode " + nbt + " into an aura connectable capability but it's not even an nbt list? what am I supposed to do with this? dunno man");
		
		connections.clear();
		
		for(NBTBase e : (NBTTagList) nbt) {
			NBTTagCompound entry = (NBTTagCompound) e;
			
			EnumFacing whichWay = EnumFacing.values()[entry.getInteger("facing")];
			ConnectionData data = new ConnectionData(entry);
			connections.put(whichWay, data);
		}
	}
}
