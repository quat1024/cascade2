package quaternary.cascade2.cap;

import net.minecraft.nbt.NBTBase;

/** 
 * Provided for convenience so I can use a general purpose IStorage class (see CascadeCapabilities)
 * I don't know what I'm doing. This feels wrong. Please correct me.
 * */
public interface ISaveLoadCapability<T> {
	NBTBase writeNBT();
	void readNBT(NBTBase nbt);
}
