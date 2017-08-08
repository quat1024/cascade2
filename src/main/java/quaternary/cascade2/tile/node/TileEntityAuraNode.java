package quaternary.cascade2.tile.node;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import quaternary.cascade2.Cascade;
import quaternary.cascade2.block.CascadeBlockTileEntity;
import quaternary.cascade2.block.node.BlockAuraNode;

import java.util.Arrays;
import java.util.List;

public class TileEntityAuraNode extends TileEntity implements ITickable {
	
	//settings
	private static final int CONNECTION_RANGE = 16;
	
	//per-node props
	public long aura = 0L;
	public boolean connectable = true;
	
	//node nbt stuff
	//The amount of aura contained in the node.
	private static final String AURA_KEY = "aura";
	//Whether the node should be connectable.
	//The node might not be connectable because someone is busy
	//breaking the block, for example
	private static final String CONNECTABLE_KEY = "connectable";
	
	//connection storage stuff
	private TileEntityAuraNode[] connectedTE = new TileEntityAuraNode[6];
	
	//ticky stuff
	public void update() {
		
	}
	
	//Temp!!!!!!!!
	public void onActivated(World www, EntityPlayer player) {
		resetAllConnections();
		
		www.playSound(player, pos, SoundEvents.BLOCK_ANVIL_BREAK, SoundCategory.BLOCKS, 1, 1);
		
		if(world.isRemote) return;
		
		Cascade.LOGGER.info("Verifying all connections:");
		removeInvalidConnections();
		Cascade.LOGGER.info("Printing connections:");
		for(int i=0; i < 6; i++) {
			if(connectedTE[i] == null) { continue; }
			Cascade.LOGGER.info("Side: " + EnumFacing.values()[i] + " Pos: " + connectedTE[i].pos.toString());
		}
	}
	
	//connection managing
	private void resetAllConnections() {
		for(int sideIndex = 0; sideIndex < 6; sideIndex++) {
			EnumFacing whichSide = EnumFacing.values()[sideIndex];
			for(int dist = 1; dist < CONNECTION_RANGE; dist++) {
				BlockPos otherPos = pos.offset(whichSide,dist);
				if(!world.isBlockLoaded(otherPos)) continue;
				
				//Can't connect through solid blocks.
				if(!canConnectionPassThrough(world.getBlockState(otherPos))) break;
				
				TileEntity checkedTE = world.getTileEntity(otherPos);
				if(checkedTE instanceof TileEntityAuraNode) {
					//Found one? Nice.
					TileEntityAuraNode otherTE = (TileEntityAuraNode) checkedTE;
					//Connect to it.
					if(otherTE.connectable) {
						this.setConnection(otherTE, whichSide);
						otherTE.setConnection(this, whichSide.getOpposite());
						//then move on to the next direction
						break;
					}
				}
			}
		}
	}
	
	private void removeInvalidConnections() {
		for(int sideIndex = 0; sideIndex < 6; sideIndex++) {
			EnumFacing whichSide = EnumFacing.values()[sideIndex];
			TileEntityAuraNode otherNode = connectedTE[sideIndex];
			if(otherNode == null) continue;
			
			//Just pretend it exists for now x)
			if(!world.isBlockLoaded(otherNode.pos)) continue;
			
			//Verify that the connected node really exists
			TileEntity realOtherNode = world.getTileEntity(otherNode.pos);
			if(realOtherNode == null) {
				Cascade.LOGGER.info("Removing dead connection on my " + whichSide.getName().toLowerCase() + " side");
				this.removeConnection(whichSide);
				continue;
			}
			
			//Verify that the node connection is unobstructed
			//TODO: this is messy.
			for(int dist = 1; dist < CONNECTION_RANGE; dist++) {
				BlockPos posToCheck = pos.offset(whichSide, dist);
				if(!world.isBlockLoaded(posToCheck)) continue;
				
				//Stop looking if I found the tile entity i'm checking the connection to.
				TileEntity blah = world.getTileEntity(posToCheck);
				if(blah != null && blah.equals(connectedTE[sideIndex])) break;
				
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
		
		for(int sideIndex = 0; sideIndex < 6; sideIndex++) {
			TileEntityAuraNode otherNode = connectedTE[sideIndex];
			if(otherNode == null) continue;
			otherNode.resetAllConnections();
		}
	}
	
	//connection managing helper functions
	private void removeConnection(int index) {
		connectedTE[index] = null;
	}
	
	private void removeConnection(EnumFacing side) {
		removeConnection(side.getIndex());
	}
	
	private void setConnection(TileEntityAuraNode other, int index) {
		connectedTE[index] = other;
	}
	
	private void setConnection(TileEntityAuraNode other, EnumFacing side) {
		setConnection(other, side.getIndex());
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
		return super.writeToNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		//todo: aura types
		aura = nbt.getLong(AURA_KEY);
		super.readFromNBT(nbt);
	}
}
