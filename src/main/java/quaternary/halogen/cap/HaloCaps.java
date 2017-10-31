package quaternary.halogen.cap;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import quaternary.halogen.aura.cap.*;
import quaternary.halogen.aura.cap.impl.AuraReceiverCap;
import quaternary.halogen.aura.cap.impl.AuraStorageCap;
import quaternary.halogen.aura.cap.impl.emitter.AuraEmitterCap;

import javax.annotation.Nullable;

public class HaloCaps {
	public static void registerCaps() {
		CapabilityManager.INSTANCE.register(IAuraStorage.class, new DefaultIStorage<>(), AuraStorageCap.class);
		CapabilityManager.INSTANCE.register(IAuraEmitter.class, new DefaultIStorage<>(), AuraEmitterCap.class);
		CapabilityManager.INSTANCE.register(IAuraReceiver.class, new DefaultIStorage<>(), AuraReceiverCap.class);
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
