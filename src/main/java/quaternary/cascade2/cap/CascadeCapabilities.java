package quaternary.cascade2.cap;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import quaternary.cascade2.cap.aura.AuraHolderBase;
import quaternary.cascade2.cap.aura.IAuraHolder;
import quaternary.cascade2.aura.type.AuraType;
import quaternary.cascade2.aura.type.AuraTypeRegistry;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CascadeCapabilities {
	@CapabilityInject(IAuraHolder.class)
	public static Capability<IAuraHolder> AURA_HOLDER = null;
	
	//public static Capability<bepis> AURA_PROVIDER = null;
	
	//public static Capability<bepis> AURA_ACCEPTOR = null;
	
	public static void registerCapabilities() {
		CapabilityManager.INSTANCE.register(IAuraHolder.class, new DefaultIStorage<>(), () -> (new AuraHolderBase(1000)));
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
