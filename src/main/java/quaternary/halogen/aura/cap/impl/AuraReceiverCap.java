package quaternary.halogen.aura.cap.impl;

import net.minecraft.nbt.NBTBase;
import quaternary.halogen.Halogen;
import quaternary.halogen.aura.cap.*;
import quaternary.halogen.aura.type.AuraType;

public class AuraReceiverCap implements IAuraReceiver {
	
	private boolean canReceive;
	private IAuraStorage storage;
	public boolean shouldUpdateComparator = false; //TODO
	
	public AuraReceiverCap(IAuraStorage storage) {
		this.storage = storage;
		canReceive = true;
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
		return canReceive && storage.hasSpace();
	}
	
	@Override
	public boolean canReceiveAura(AuraType type, int amt) {
		return isEligible() && storage.canAddAura(type, amt);
	}
	
	@Override
	public void receiveAura(AuraType type, int amt, IAuraEmitter emitter) {
		Halogen.LOGGER.info("RECEEEEVER adding " + amt + " aura");
		Halogen.LOGGER.info("Before " + storage.getTotalAura());
		storage.addAura(type, amt);
		Halogen.LOGGER.info("After " + storage.getTotalAura());
		shouldUpdateComparator = true;
	}
	
	@Override
	public NBTBase writeNBT() {
		return null;
	}
	
	@Override
	public void readNBT(NBTBase nbt) {
		
	}
}
