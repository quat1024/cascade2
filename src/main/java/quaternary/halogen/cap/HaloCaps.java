package quaternary.halogen.cap;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import quaternary.halogen.cap.aura.IAuraEmitter;
import quaternary.halogen.cap.aura.IAuraReceiver;
import quaternary.halogen.cap.aura.impl.AuraEmitterCap;
import quaternary.halogen.cap.aura.impl.AuraReceiverCap;
import quaternary.halogen.cap.aura.impl.AuraStorageCap;
import quaternary.halogen.cap.aura.IAuraStorage;

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
