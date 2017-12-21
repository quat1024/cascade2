package quaternary.halogen.aura.cap.impl;

import net.minecraft.nbt.NBTBase;
import quaternary.halogen.aura.cap.IAuraReceiver;
import quaternary.halogen.aura.cap.IAuraStorage;

public class AuraReceiverCap implements IAuraReceiver {
	IAuraStorage storageCap;
	public AuraReceiverCap(IAuraStorage storageCap) {
		this.storageCap = storageCap;
	}
	
	@Override
	public NBTBase writeNBT() {
		return null;
	}
	
	@Override
	public void readNBT(NBTBase nbt) {
		
	}
}
