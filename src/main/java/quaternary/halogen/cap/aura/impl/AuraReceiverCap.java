package quaternary.halogen.cap.aura.impl;

import net.minecraft.nbt.NBTBase;
import quaternary.halogen.aura.type.AuraType;
import quaternary.halogen.cap.aura.IAuraEmitter;
import quaternary.halogen.cap.aura.IAuraReceiver;
import quaternary.halogen.cap.aura.IAuraStorage;

public class AuraReceiverCap implements IAuraReceiver {
	
	private boolean canReceive;
	private IAuraStorage storage;
	
	public AuraReceiverCap(IAuraStorage storage) {
		this.storage = storage;
	}
	
	@Override
	public IAuraStorage getStorage() {
		return storage;
	}
	
	@Override
	public void setEligible(boolean canReceive) {
		this.canReceive = canReceive;
	}
	
	@Override
	public boolean isEligible() {
		return canReceive;
	}
	
	@Override
	public boolean canReceiveAura(AuraType type, int amt) {
		return
			isEligible() &&
			storage.canAddAura(type, amt);
	}
	
	@Override
	public void receiveAura(AuraType type, int amt, IAuraEmitter emitter) {
		storage.addAura(type, amt);
	}
	
	@Override
	public NBTBase writeNBT() {
		return null;
	}
	
	@Override
	public void readNBT(NBTBase nbt) {
		
	}
}
