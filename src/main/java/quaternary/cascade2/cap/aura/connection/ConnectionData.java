package quaternary.cascade2.cap.aura.connection;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

/** Data class which represents a connection from an IAuraConnectable to another.
 * TODO: Entities don't have a BlockPos; rounding to the nearest BlockPos is not acceptable.
 * That needs to be figured out. Fortunately because of the wonders of :tada: abstraction :tada:
 * it should just be possible to split this into an interface or something.
 * 
 * IConnectionData
 *  +- BlockConnectionData
 *  +- EntityConnectionData (item and entity? those feel the same
 * 
 * */
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