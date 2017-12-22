package quaternary.halogen.cap;

import net.minecraft.nbt.NBTTagCompound;

/**
 * A capability that can save and load itself to NBT.
 * Implementations must wrap themselves in an NBTTagCompound.
 */
public interface ISaveLoadCapability {
	NBTTagCompound writeNBT();
	
	void readNBT(NBTTagCompound nbt);
}
