package quaternary.halogen.cap.aura.impl;

import net.minecraft.nbt.NBTBase;
import quaternary.halogen.aura.type.AuraType;
import quaternary.halogen.cap.aura.IAuraEmitter;
import quaternary.halogen.cap.aura.IAuraReceiver;
import quaternary.halogen.cap.aura.IAuraStorage;

public class AuraEmitterCap implements IAuraEmitter {
	
	private boolean canEmit;
	private IAuraStorage storage;
	
	public AuraEmitterCap(IAuraStorage storage) {
		this.storage = storage;
		canEmit = true;
	}
	
	@Override
	public IAuraStorage getStorage() {
		return storage;
	}
	
	@Override
	public void setEligible(boolean canEmit) {
		this.canEmit = canEmit;
	}
	
	@Override
	public boolean isEligible() {
		return canEmit && storage.hasAura();
	}
	
	@Override
	public boolean canEmitAura(AuraType type, int amt, IAuraReceiver reciever) {
		return
			isEligible() &&
			!(reciever == null) &&
			reciever.isEligible() &&
			storage.getTotalAura() > reciever.getStorage().getTotalAura() &&
			storage.canRemoveAura(type, amt) &&
			reciever.canReceiveAura(type, amt);
	}
	
	@Override
	public void emitAura(AuraType type, int amt, IAuraReceiver receiver) {
		storage.removeAura(type, amt);
		receiver.receiveAura(type, amt, this);
	}
	
	@Override
	public NBTBase writeNBT() {
		return null;
	}
	
	@Override
	public void readNBT(NBTBase nbt) {
		
	}
}
