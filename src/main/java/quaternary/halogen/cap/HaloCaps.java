package quaternary.halogen.cap;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class HaloCaps {
	public static void registerCaps() {
		//Do the thing!
	}
	
	static class DefaultIStorage<T extends ISaveLoadCapability> implements Capability.IStorage<T> {
		@Nullable
		@Override
		public NBTBase writeNBT(Capability<T> capability, T instance, EnumFacing side) {
			return instance.writeNBT();
		}
		
		@Override
		public void readNBT(Capability<T> capability, T instance, EnumFacing side, NBTBase nbt) {
			instance.readNBT(nbt);
		}
	}
}
