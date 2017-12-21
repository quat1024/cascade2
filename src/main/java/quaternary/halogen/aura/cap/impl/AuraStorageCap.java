package quaternary.halogen.aura.cap.impl;

import net.minecraft.nbt.NBTBase;
import quaternary.halogen.aura.cap.IAuraStorage;

public class AuraStorageCap implements IAuraStorage {
	int max;
	public AuraStorageCap(int max) {
		this.max = max;
	}
	
	@Override
	public NBTBase writeNBT() {
		return null;
	}
	
	@Override
	public void readNBT(NBTBase nbt) {
		
	}
}
