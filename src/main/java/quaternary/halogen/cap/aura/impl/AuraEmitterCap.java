package quaternary.halogen.cap.aura.impl;

import net.minecraft.nbt.NBTBase;
import quaternary.halogen.*;
import quaternary.halogen.aura.type.AuraType;
import quaternary.halogen.cap.aura.IAuraEmitter;
import quaternary.halogen.cap.aura.IAuraReceiver;
import quaternary.halogen.cap.aura.IAuraStorage;
import quaternary.halogen.util.*;

import javax.annotation.*;

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
	public boolean canEmitAura(AuraType type, int amt, @Nonnull IAuraReceiver receiver) {
		return doEmitAura(type, amt, receiver, false);
	}
	
	@Override
	public void emitAura(AuraType type, int amt, @Nonnull IAuraReceiver receiver) {
		doEmitAura(type, amt, receiver, true);
	}
	
	private boolean doEmitAura(AuraType type, int amt, @Nonnull IAuraReceiver receiver, boolean forReal) {
		if(isEligible() && receiver.isEligible()) {
			int auraDifference = storage.getTotalAura() - receiver.getStorage().getTotalAura();
			if(auraDifference > 1) {
				int auraAvg = (storage.getTotalAura() + receiver.getStorage().getTotalAura())/2;
				
				int toEmit = Utils.min(amt, storage.getTotalAura() - auraAvg, storage.getTotalAura(), receiver.getStorage().getRemainingSpace());
				if(toEmit != 0) {
					if(forReal) {
						storage.removeAura(type, toEmit);
						receiver.receiveAura(type, toEmit, this);
					}
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public NBTBase writeNBT() {
		return null;
	}
	
	@Override
	public void readNBT(NBTBase nbt) {
		
	}
}
