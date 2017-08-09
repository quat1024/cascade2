package quaternary.cascade2.tile.node;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import quaternary.cascade2.Cascade;
import quaternary.cascade2.net.util.CascadePacketUtils;
import quaternary.cascade2.tile.CascadeTileEntity;
import quaternary.cascade2.util.CascadeUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//This class is fkin huge but things will get split off eventually, don't worry

public class TileEntityAuraNode extends CascadeTileEntity implements ITickable {
	
	//global setting stuff
	private static final int MAX_AURA = 1000;
	private static final int CONNECTION_RANGE = 16;
	private static final AxisAlignedBB ITEM_DETECTION_AABB = new AxisAlignedBB(0,0,0,1,1,1);
	
	@GameRegistry.ObjectHolder("cascade2:aura_crystal")
	public static final Item AURA_CRYSTAL_ITEM = null;
	
	//per-node prop stuff
	public int aura = 0;
	public boolean connectable = true;
	//ticks until another aura crystal can be accepted
	public byte auraCooldown = 0;
	
	//node nbt stuff
	private static final String AURA_KEY = "Aura";
	private static final String AURA_COOLDOWN_KEY = "AuraCooldown";
	private static final String CONNECTABLE_KEY = "Connectable";
	private static final String CONNECTIONS_LIST_KEY = "ConnectionList";
	private static final String FACING_KEY = "Facing";
	
	//connection storage stuff
	private ConcurrentHashMap<EnumFacing, BlockPos> connectedTEMap = new ConcurrentHashMap<>();
	
	//packet stuff
	//todo: am I doing this right? I'm basically stealing what botania does shh
	private boolean shouldDispatchPacket;
	
	//ticky tock stuff
	public void update() {
		if(!world.isRemote) {
			if(shouldDispatchPacket) {
				shouldDispatchPacket = false;
				CascadePacketUtils.dispatchTEToNearby(this);
			}
			
			if(auraCooldown > 0) auraCooldown--;
			
			if(auraCooldown == 0) {
				List<EntityItem> nearbyItems = world.getEntitiesWithinAABB(EntityItem.class, ITEM_DETECTION_AABB.offset(this.pos));
				nearbyItems.removeIf(item -> item.getItem().isEmpty() || !item.getItem().isItemEqual(new ItemStack(AURA_CRYSTAL_ITEM)));
				if(!nearbyItems.isEmpty()) {
					for(EntityItem ent : nearbyItems) {
						ItemStack stack = ent.getItem();
						if(MAX_AURA - aura > 50) { //Todo: aura types
							aura += 50;       //Todo: make this per-crystal, not a hardcoded 50
							auraCooldown = 20;
							stack.shrink(1);
							Cascade.LOGGER.info("New size: " + stack.getCount());
							break;
						}
					}
				}
			}
		}
	}
	
	@Override
	public void onLoad() {
		if(!world.isRemote) this.resetAllConnections();
	}
	
	//Temp!!!!!!!!
	public void onActivated(World www, EntityPlayer player) {
		/*if(player.isSneaking()) {
			Cascade.LOGGER.info("Rechecking all connections from scratch:");
			resetAllConnections();
		} else {
			Cascade.LOGGER.info("Verifying all connections:");
			removeInvalidConnections();
		}
		
		Cascade.LOGGER.info("Printing connections:");
		
		for(Map.Entry<EnumFacing,BlockPos> pair : connectedTEMap.entrySet()) {
			Cascade.LOGGER.info("Side: " + pair.getKey() + " Pos: " + pair.getValue());
		}*/
	}
	
	//connection managing
	private void resetAllConnections() {
		if(world.isRemote) return;
		
		clearConnections();
		
		for(int sideIndex = 0; sideIndex < EnumFacing.values().length; sideIndex++) {
			EnumFacing whichSide = EnumFacing.values()[sideIndex];
			for(int dist = 1; dist < CONNECTION_RANGE; dist++) {
				BlockPos otherPos = pos.offset(whichSide,dist);
				if(!world.isBlockLoaded(otherPos)) continue;
				
				//Can't connect through solid blocks.
				if(!canConnectionPassThrough(world.getBlockState(otherPos))) break;
				
				TileEntity checkedTE = world.getTileEntity(otherPos); //not using my getloadedtileentity b/c already checked loadedness up there
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
		if(world.isRemote) return;
		
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
				this.removeConnection(pair.getKey());
				continue;
			}
			
			//Is the connection to this node unobstructed?
			int distToOther = CascadeUtils.blockPosDistance(pos, otherPos);
			for(int dist = 1; dist < distToOther; dist++) {
				BlockPos posToCheck = pos.offset(whichSide, dist);
				if(!world.isBlockLoaded(posToCheck)) continue;
				
				if(!canConnectionPassThrough(world.getBlockState(posToCheck))) {
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
	
	//helper functions for managing connections
	private void removeConnection(EnumFacing side) {
		if(connectedTEMap.get(side) != null) {
			connectedTEMap.remove(side);
			shouldDispatchPacket = true;			
		}
	}
	
	private void setConnection(EnumFacing side, BlockPos otherPos) {
		if((connectedTEMap.get(side) == null && otherPos != null) || !connectedTEMap.get(side).equals(otherPos)) {
			connectedTEMap.put(side, otherPos);
			shouldDispatchPacket = true;
		}
	}
	
	private BlockPos getConnection(EnumFacing side) {
		return connectedTEMap.get(side);
	}
	
	private void clearConnections() {
		if(!connectedTEMap.isEmpty()) {
			connectedTEMap.clear();
			shouldDispatchPacket = true;
		}
	}
	
	public ConcurrentHashMap<EnumFacing, BlockPos> getConnectionMap() {
		return connectedTEMap;
	}
	
	public void replaceConnectionMap(ConcurrentHashMap<EnumFacing, BlockPos> newMap) {
		clearConnections();
		connectedTEMap.putAll(newMap);
		shouldDispatchPacket = true;
	}
	
	private boolean canConnectionPassThrough(IBlockState state) {
		if(state.getMaterial().isReplaceable() || state.getMaterial().isLiquid()) return true;
		return !(state.isOpaqueCube() || state.isFullCube() || state.isFullBlock());
	}
	
	//nbt stuff
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		//todo: aura types
		nbt.setInteger(AURA_KEY, aura);
		nbt.setBoolean(CONNECTABLE_KEY, connectable);
		nbt.setByte(AURA_COOLDOWN_KEY, auraCooldown);
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
		aura = nbt.getInteger(AURA_KEY);
		connectable = nbt.getBoolean(CONNECTABLE_KEY);
		auraCooldown = nbt.getByte(AURA_COOLDOWN_KEY);
		
		connectedTEMap.clear();
		NBTTagList connectionsList = nbt.getTagList(CONNECTIONS_LIST_KEY, 10);
		for(NBTBase e : connectionsList) {
			NBTTagCompound entry = (NBTTagCompound) e;
			BlockPos connectPos = new BlockPos(
							entry.getInteger("x"),
							entry.getInteger("y"),
							entry.getInteger("z"));
			EnumFacing whichWay = EnumFacing.values()[entry.getInteger(FACING_KEY)];
			
			setConnection(whichWay, connectPos);
		}
		super.readFromNBT(nbt);
	}
}
