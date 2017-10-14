package quaternary.halogen.cap;

import net.minecraft.nbt.NBTBase;

/**
 * A capability that can save and load itself to NBT.
 */
public interface ISaveLoadCapability {
	NBTBase writeNBT();
	
	void readNBT(NBTBase nbt);
}
