package quaternary.cascade2.tile.node;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import quaternary.cascade2.Cascade;
import quaternary.cascade2.tile.CascadeTileEntity;
import quaternary.cascade2.util.CascadeUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TileEntityAuraNode extends CascadeTileEntity implements ITickable {
	
	//global setting stuff
	private static final int CONNECTION_RANGE = 16;
	
	//per-node prop stuff
	public long aura = 0L;
	public boolean connectable = true;
	
	//node nbt stuff
	private static final String AURA_KEY = "aura";
	private static final String CONNECTABLE_KEY = "connectable";
	private static final String CONNECTIONS_LIST_KEY = "connections_list";
	private static final String FACING_KEY = "facing";
	
	//connection storage stuff
	private ConcurrentHashMap<EnumFacing, BlockPos> connectedTEMap = new ConcurrentHashMap<>();
	
	//ticky tock stuff
	public void update() {
		
	}
	
	@Override
	public void onLoad() {
	}
	
	//Temp!!!!!!!!
	public void onActivated(World www, EntityPlayer player) {
		if(world.isRemote) return;
		
		if(player.isSneaking()) {
			Cascade.LOGGER.info("Rechecking all connections from scratch:");
			resetAllConnections();
		} else {
			Cascade.LOGGER.info("Verifying all connections:");
			removeInvalidConnections();
		}
		
		Cascade.LOGGER.info("Printing connections:");
		
		for(Map.Entry<EnumFacing,BlockPos> pair : connectedTEMap.entrySet()) {
			Cascade.LOGGER.info("Side: " + pair.getKey() + " Pos: " + pair.getValue());
		}
	}
	
	//connection managing
	private void resetAllConnections() {
		clearConnections();
		
		for(int sideIndex = 0; sideIndex < EnumFacing.values().length; sideIndex++) {
			EnumFacing whichSide = EnumFacing.values()[sideIndex];
			for(int dist = 1; dist < CONNECTION_RANGE; dist++) {
				BlockPos otherPos = pos.offset(whichSide,dist);
				if(!world.isBlockLoaded(otherPos)) continue;
				
				//Can't connect through solid blocks.
				if(!canConnectionPassThrough(world.getBlockState(otherPos))) break;
				
				TileEntity checkedTE = world.getTileEntity(otherPos); //not using my getloadedtileentity b/c already checked up there
				if(checkedTE instanceof TileEntityAuraNode) {
					TileEntityAuraNode otherTE = (TileEntityAuraNode) checkedTE;
					if(otherTE.connectable) {
						this.setConnection(whichSide, otherTE.pos);
						otherTE.setConnection(whichSide.getOpposite(), this.pos);
						break;
					}
				}
			}
		}
	}
	
	private void removeInvalidConnections() {		
		for(Map.Entry<EnumFacing,BlockPos> pair : connectedTEMap.entrySet()) {
			EnumFacing whichSide = pair.getKey();
			BlockPos otherPos = pair.getValue();
			TileEntityAuraNode other = (TileEntityAuraNode) CascadeUtils.getLoadedTileEntity(world, otherPos);
			
			//Intentionally NOT checking for loadedness first
			//because this removes the mapping from the hashmap.
			//I will only ever check an unloaded chunk once.
			//And, I don't want to remove a connection, just because it is unloaded.
			//Otherwise stuff across chunk borders would just fall apart super fast.
			if(other != null && world.getTileEntity(otherPos) == null) {
				Cascade.LOGGER.info("Removing dead connection on my " + whichSide.getName() + " side");
				this.removeConnection(pair.getKey());
				continue;
			}
			
			//Is the connection to this node unobstructed?
			int distToOther = CascadeUtils.blockPosDistance(pos, otherPos);
			for(int dist = 1; dist < distToOther; dist++) {
				BlockPos posToCheck = pos.offset(whichSide, dist);
				if(!world.isBlockLoaded(posToCheck)) continue;
				
				if(!canConnectionPassThrough(world.getBlockState(posToCheck))) {
					Cascade.LOGGER.info("Removing connection to my " + whichSide.getName().toLowerCase() + " side because it is obstructed");
					this.removeConnection(whichSide);
					break;
				}
			}
		}
		
		//todo: Do I need to verify anything else about the connections?
	}
	
	//block interactions
	public void onBreakBlock() {
		this.connectable = false;
		
		for(Map.Entry<EnumFacing,BlockPos> pair : connectedTEMap.entrySet()) {
			TileEntityAuraNode other = (TileEntityAuraNode) CascadeUtils.getLoadedTileEntity(world, pair.getValue());
			if(other == null) continue;
			other.resetAllConnections();
		}
	}
	
	public void onPlaceBlock() {
		resetAllConnections();
	}
	
	private void removeConnection(EnumFacing side) {
		connectedTEMap.remove(side);
	}
	
	private void setConnection(EnumFacing side, BlockPos otherPos) {
		connectedTEMap.put(side, otherPos);
	}
	
	private BlockPos getConnection(EnumFacing side) {
		return connectedTEMap.get(side);
	}
	
	private void clearConnections() {
		connectedTEMap.clear();
	}
	
	public ConcurrentHashMap<EnumFacing, BlockPos> getConnections() {
		return connectedTEMap;
	}
	
	private boolean canConnectionPassThrough(IBlockState state) {
		if(state.getMaterial().isReplaceable() || state.getMaterial().isLiquid()) return true;
		return !(state.isOpaqueCube() || state.isFullCube() || state.isFullBlock());
	}
	
	//nbt stuff
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		//todo: aura types
		nbt.setLong(AURA_KEY, aura);
		nbt.setBoolean(CONNECTABLE_KEY, connectable);
		
		NBTTagList connectionsList = new NBTTagList();
		for(Map.Entry<EnumFacing,BlockPos> pair : connectedTEMap.entrySet()) {
			EnumFacing whichWay = pair.getKey();
			BlockPos whichPos = pair.getValue();
			
			NBTTagCompound connectionCompound = new NBTTagCompound();
			connectionCompound.setInteger(FACING_KEY, whichWay.getIndex());
			connectionCompound.setInteger("x", whichPos.getX());
			connectionCompound.setInteger("y", whichPos.getY());
			connectionCompound.setInteger("z", whichPos.getZ());
			connectionsList.appendTag(connectionCompound);
		}
		nbt.setTag(CONNECTIONS_LIST_KEY, connectionsList);
		
		return super.writeToNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		//todo: aura types
		aura = nbt.getLong(AURA_KEY);
		connectable = nbt.getBoolean(CONNECTABLE_KEY);
		
		//This is JUST FOR DEBUGGING
		//I don't need to set my own pos this early
		pos = CascadeUtils.blockPosFromNBTCompound(nbt);
		Cascade.LOGGER.info("Hello, I am " + pos.toString());
		
		connectedTEMap.clear();
		NBTTagList connectionsList = nbt.getTagList(CONNECTIONS_LIST_KEY, 10);
		for(NBTBase e : connectionsList) {
			NBTTagCompound entry = (NBTTagCompound) e;
			BlockPos connectPos = new BlockPos(
							entry.getInteger("x"),
							entry.getInteger("y"),
							entry.getInteger("z"));
			EnumFacing whichWay = EnumFacing.values()[entry.getInteger(FACING_KEY)];
			
			Cascade.LOGGER.info("Loading " + whichWay.getName() + " facing connection to " + connectPos.toString());
			setConnection(whichWay, connectPos);
		}
		super.readFromNBT(nbt);
	}
}
