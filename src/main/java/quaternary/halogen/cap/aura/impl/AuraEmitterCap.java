package quaternary.halogen.cap.aura.impl;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.math.MathHelper;
import quaternary.halogen.aura.type.AuraType;
import quaternary.halogen.cap.aura.*;
import quaternary.halogen.util.Utils;

import javax.annotation.Nonnull;

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
				int auraAvg = MathHelper.ceil((storage.getTotalAura() + receiver.getStorage().getTotalAura())/2f);
				
				int toEmit = Utils.min(storage.getTotalAura() - auraAvg, amt, storage.getTotalAura(), receiver.getStorage().getRemainingSpace());
				
				if(toEmit != 0) {
					if(forReal) {
						storage.removeAura(type, toEmit);
						receiver.receiveAura(type, toEmit, this);
					}
					return true;
				}
			} else if (auraDifference == 1) {
				//Special case: if there's only one aura left in me, just remove it
				if(receiver.getStorage().getTotalAura() == 0) {
					storage.removeAura(type, 1);
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
