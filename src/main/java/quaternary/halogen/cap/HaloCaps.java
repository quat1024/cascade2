package quaternary.halogen.cap;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import quaternary.halogen.aura.cap.*;
import quaternary.halogen.aura.cap.impl.*;
import quaternary.halogen.misc.UhhhhhException;

import javax.annotation.Nullable;

public class HaloCaps {
	public static void registerCaps() {
		CapabilityManager.INSTANCE.register(IAuraStorage.class, new DefaultIStorage<>(), AuraStorageCap.class);
		CapabilityManager.INSTANCE.register(IAuraEmitter.class, new DefaultIStorage<>(), AuraEmitterCap.class);
		CapabilityManager.INSTANCE.register(IAuraReceiver.class, new DefaultIStorage<>(), AuraReceiverCap.class);
	}
	
	//Halogen capabilities only read to and write NBTTagCompounds.
	static class DefaultIStorage<T extends ISaveLoadCapability> implements Capability.IStorage<T> {
		@Nullable
		@Override
		public NBTBase writeNBT(Capability<T> capability, T instance, EnumFacing side) {
			return instance.writeNBT();
		}
		
		@Override
		public void readNBT(Capability<T> capability, T instance, EnumFacing side, NBTBase nbt) {
			if(nbt instanceof NBTTagCompound)	instance.readNBT((NBTTagCompound) nbt);
			else throw new UhhhhhException("Uhmmmm, tried to deserialize a " + nbt.getClass().getSimpleName() + " in a capability but I only know how to deal with NBTTagCompounds");
		}
	}
}
