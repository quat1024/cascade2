package quaternary.cascade2.tile.node;
// Comment with space after the slashes to appease Nerxit.

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import quaternary.cascade2.Cascade;
import quaternary.cascade2.aura.AuraContainerNotCapabilityDoNotUseSeriously;
import quaternary.cascade2.aura.type.AuraType;
import quaternary.cascade2.aura.crystal.IAuraCrystal;
import quaternary.cascade2.net.util.CascadePacketUtils;
import quaternary.cascade2.tile.CascadeTileEntity;
import quaternary.cascade2.util.CascadeUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//This class is fkin huge but things will get split off eventually, don't worry

public class TileEntityAuraNode extends CascadeTileEntity implements ITickable {
	
	//global setting stuff
	private static final int CONNECTION_RANGE = 16;
	private static final AxisAlignedBB ITEM_DETECTION_AABB = new AxisAlignedBB(0, 0, 0, 1, 1, 1);
	
	//per-node prop stuff
	public AuraContainerNotCapabilityDoNotUseSeriously auraContainer = new AuraContainerNotCapabilityDoNotUseSeriously(1000);
	public boolean connectable = true;
	//ticks until another aura crystal can be accepted
	public byte auraAbsorptionCooldown = 0;
	
	//node nbt stuff
	private static final String AURA_KEY = "Aura";
	private static final String AURA_COOLDOWN_KEY = "AuraCooldown";
	private static final String CONNECTABLE_KEY = "Connectable";
	private static final String CONNECTIONS_LIST_KEY = "ConnectionList";
	private static final String FACING_KEY = "Facing";
	
	//connection storage stuff
	/** Stores all connections from this aura container to other containers. */
	private ConcurrentHashMap<EnumFacing, BlockPos> connectionMap = new ConcurrentHashMap<>();
	/** Stores whether the connection is "active", i.e. unobstructed. */
	private ConcurrentHashMap<EnumFacing, Boolean> activeMap = new ConcurrentHashMap<>();
	
	//packet stuff
	//todo: am I doing this right? I'm basically stealing what botania does shh
	private boolean shouldDispatchPacket;
	
	//render aabb stuff
	@SideOnly(Side.CLIENT)
	private boolean dirtyAABB;
	@SideOnly(Side.CLIENT)
	private AxisAlignedBB renderAABB;
	
	public void update() {
		if(!world.isRemote) {
			//FIXME: Don't use hacky bullshit.
			if(shouldDispatchPacket) {
				CascadePacketUtils.dispatchTEToNearby(this);
				shouldDispatchPacket = false;
			}
			
			if(world.getWorldTime() % 20 == 0) {
				updateConnections();
			}
			
			if(auraAbsorptionCooldown > 0) auraAbsorptionCooldown--;
			
			if(auraAbsorptionCooldown == 0) {
				List<EntityItem> nearbyItems = world.getEntitiesWithinAABB(EntityItem.class, ITEM_DETECTION_AABB.offset(this.pos));
				
				//FIXME: Capabilities instead of instanceof checks
				nearbyItems.removeIf(itemEntity -> itemEntity.getItem/*Stack*/().isEmpty() || !(itemEntity.getItem/*Stack*/().getItem() instanceof IAuraCrystal));
				
				if(!nearbyItems.isEmpty()) {
					for(EntityItem ent : nearbyItems) {
						ItemStack stack = ent.getItem/*Stack*/();
						IAuraCrystal crystalItem = (IAuraCrystal) stack.getItem();
						
						AuraType type = crystalItem.getAuraType(stack);
						int auraToAdd = crystalItem.getAuraContained(stack);
						
						if(auraContainer.canAddAuraAmount(auraToAdd)) {
							auraContainer.addAuraOfType(type, auraToAdd);
							auraAbsorptionCooldown = 20;
							stack.shrink(1);
							break; //only eat one stack at a time
						}
					}
				}
			}
		}
	}
	
	@Override
	public void onLoad() {
		if(world.isRemote) {
			dirtyAABB = true;
		} else {
			this.resetAllConnections();
		}
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
		*/
		if(!world.isRemote) return;
		
		player.sendMessage(new TextComponentString("My name Jeff!!!! xD"));
		
		for(Map.Entry<EnumFacing, BlockPos> pair : connectionMap.entrySet()) {
			EnumFacing whichWay = pair.getKey();
			BlockPos whatPos = pair.getValue();
			//This SHOUDN'T cause any problems because the hashmaps SHOULD be parallel.
			//It looks really fuckin scary though. Right? Tell me that doesn't look scary.
			boolean active = activeMap.get(whichWay);
			boolean active2 = isConnectionActive(whichWay);
			player.sendMessage(new TextComponentString("Side: " + whichWay + " Pos: " + whatPos + " Active: " + active2));
		}
	}
	
	//rendering stuff
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox() {
		if(dirtyAABB || renderAABB == null) recalculateRenderBoundingBox();
		return renderAABB;
	}
	
	private void recalculateRenderBoundingBox() {
		int lengthX = hasConnection(EnumFacing.EAST) ? getDistanceToConnection(EnumFacing.EAST) : 0;
		int lengthY = hasConnection(EnumFacing.UP) ? getDistanceToConnection(EnumFacing.UP) : 0;
		int lengthZ = hasConnection(EnumFacing.SOUTH) ? getDistanceToConnection(EnumFacing.SOUTH) : 0;
		
		renderAABB = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(),
						pos.getX() + lengthX + 1, pos.getY() + lengthY + 1, pos.getZ() + lengthZ + 1);
		dirtyAABB = false;
	}
	
	//connection managing
	private void resetAllConnections() {
		if(world.isRemote) return;
		
		clearConnections();
		
		for(int sideIndex = 0; sideIndex < EnumFacing.values().length; sideIndex++) {
			EnumFacing whichSide = EnumFacing.values()[sideIndex];
			boolean unobstructedFlag = true;
			
			for(int dist = 1; dist < CONNECTION_RANGE; dist++) {
				BlockPos otherPos = pos.offset(whichSide, dist);
				if(!world.isBlockLoaded(otherPos)) continue;
				
				//If the connection is obstructed, mark that
				if(!canConnectionPassThrough(world.getBlockState(otherPos))) unobstructedFlag = false;
				
				//FIXME: caps
				TileEntity checkedTE = world.getTileEntity(otherPos);
				if(checkedTE instanceof TileEntityAuraNode) {
					TileEntityAuraNode otherTE = (TileEntityAuraNode) checkedTE;
					if(otherTE.connectable) {
						this.setConnection(whichSide, otherTE.pos, unobstructedFlag);
						otherTE.setConnection(whichSide.getOpposite(), this.pos, unobstructedFlag);
						break;
					}
				}
			}
		}
	}
	
	//FIXME: This method sucks ass.
	private void updateConnections() {
		if(world.isRemote) return;
		
		for(Map.Entry<EnumFacing, BlockPos> pair : connectionMap.entrySet()) {
			EnumFacing whichWay = pair.getKey();
			BlockPos otherPos = pair.getValue();
			boolean active = activeMap.get(whichWay);
			
			//Regardless of whether it's active, if the tile entity doesn't exist, remove it.
			TileEntityAuraNode other = (TileEntityAuraNode) world.getTileEntity(otherPos);
			if(other == null) {
				removeConnection(whichWay);
			}
			
			if(active) {
				//If it's marked active, but not loaded, mark it inactive.
				if(!world.isBlockLoaded(otherPos)) {
					activeMap.put(whichWay, false);
					continue;
				}
			}
			
			//If it's obstructed but active, demote it to inactive.
			//If it's unobstructed but inactive, promote it to active.
			if(isObstructed(whichWay) == active) {
				activeMap.put(whichWay, !active);
			}
		}
	}
	
	//block interactions
	public void onBreakBlock() {
		this.connectable = false;
		
		for(Map.Entry<EnumFacing, BlockPos> pair : connectionMap.entrySet()) {
			TileEntityAuraNode other = (TileEntityAuraNode) CascadeUtils.getLoadedTileEntity(world, pair.getValue());
			if(other == null) continue;
			other.resetAllConnections();
		}
	}
	
	public void onPlaceBlock() {
		resetAllConnections();
	}
	
	//helper functions for managing connections
	//These will all move into the aura system capability whenever that happens.
	//Ah, the joys of dealing with parallel data structures.
	private void removeConnection(EnumFacing side) {
		if(hasConnection(side)) {
			connectionMap.remove(side);
			activeMap.remove(side);
			shouldDispatchPacket = true;
		}
	}
	
	private void setConnection(EnumFacing side, BlockPos otherPos, boolean active) {
		if(otherPos == null) return;
		
		//If I'm not connected there, or, if I am connected but the position is different
		if(!hasConnection(side) || !getConnection(side).equals(otherPos)) {
			connectionMap.put(side, otherPos);
			activeMap.put(side, active);
			shouldDispatchPacket = true;
		}
	}
	
	public boolean hasConnection(EnumFacing side) {
		return connectionMap.containsKey(side);
	}
	
	@Nullable
	private BlockPos getConnection(EnumFacing side) {
		return connectionMap.getOrDefault(side, null);
	}
	
	private boolean isConnectionActive(EnumFacing side) {
		return activeMap.getOrDefault(side, false);
	}
	
	private void clearConnections() {
		if(!connectionMap.isEmpty()) {
			connectionMap.clear();
			activeMap.clear();
			shouldDispatchPacket = true;
		}
	}
	
	public int getDistanceToConnection(EnumFacing side) {
		if(!hasConnection(side))
			throw new IllegalArgumentException("Tried to get distance to nonexistent side " + side.getName());
		
		return CascadeUtils.blockPosDistance(pos, getConnection(side));
	}
	
	public ConcurrentHashMap<EnumFacing, BlockPos> getConnectionMap() {
		return connectionMap;
	}
	
	public void replaceConnectionMap(ConcurrentHashMap<EnumFacing, BlockPos> newMap) {
		clearConnections();
		connectionMap.putAll(newMap);
		updateConnections();
	}
	
	private boolean isObstructed(EnumFacing whichWay) {
		int distToOther = getDistanceToConnection(whichWay);
		for(int dist = 1; dist < distToOther; dist++) {
			BlockPos posToCheck = pos.offset(whichWay, dist);
			//TODO: Do I need this check?
			if(!world.isBlockLoaded(posToCheck)) continue;
			
			if(!canConnectionPassThrough(world.getBlockState(posToCheck))) {
				return true;
			}
		}
		return false;
	}
	
	private boolean canConnectionPassThrough(IBlockState state) {
		if(state.getMaterial().isReplaceable() || state.getMaterial().isLiquid()) return true;
		return !(state.isOpaqueCube() || state.isFullCube() || state.isFullBlock());
	}
	
	//nbt stuff
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setTag(AURA_KEY, auraContainer.writeToNBTList());
		nbt.setBoolean(CONNECTABLE_KEY, connectable);
		nbt.setByte(AURA_COOLDOWN_KEY, auraAbsorptionCooldown);
		NBTTagList connectionsList = new NBTTagList();
		for(Map.Entry<EnumFacing, BlockPos> pair : connectionMap.entrySet()) {
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
		auraContainer.readFromNBTList(nbt.getTagList(AURA_KEY, 10));
		connectable = nbt.getBoolean(CONNECTABLE_KEY);
		auraAbsorptionCooldown = nbt.getByte(AURA_COOLDOWN_KEY);
		
		clearConnections();
		
		NBTTagList connectionsList = nbt.getTagList(CONNECTIONS_LIST_KEY, 10);
		for(NBTBase e : connectionsList) {
			NBTTagCompound entry = (NBTTagCompound) e;
			BlockPos connectPos = new BlockPos(
							entry.getInteger("x"),
							entry.getInteger("y"),
							entry.getInteger("z"));
			EnumFacing whichWay = EnumFacing.values()[entry.getInteger(FACING_KEY)];
			
			setConnection(whichWay, connectPos, false); //assume inactive
		}
		super.readFromNBT(nbt);
		
		//for some reason world is null on the server but not client right now
		//so this is basically "world.isRemote"
		if(world != null) dirtyAABB = true;
	}
}
