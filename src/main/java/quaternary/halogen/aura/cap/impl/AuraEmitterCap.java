package quaternary.halogen.aura.cap.impl;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.math.MathHelper;
import quaternary.halogen.aura.cap.*;
import quaternary.halogen.aura.type.AuraType;
import quaternary.halogen.util.Utils;

import javax.annotation.Nonnull;

public class AuraEmitterCap implements IAuraEmitter {
	private boolean canEmit;
	IAuraStorage storage;
	
	boolean fireAll;
	
	public AuraEmitterCap(IAuraStorage storage, boolean fireAll) {
		this.storage = storage;		
		this.fireAll = fireAll;
		
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
	
	//It's a mess!
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
			/*
			int toEmit;
			if(fireAll) {
				toEmit = amt;
			} else if(auraDifference > 1) {
				//Try to divide aura evenly between myself and the other node.
				//Ofc, only send aura if I actually have *more* (hence the if up there)
				int auraAvg = MathHelper.ceil((storage.getTotalAura() + receiver.getStorage().getTotalAura()) / 2f);
				toEmit = storage.getTotalAura() - auraAvg;
			} else {
				//If I'm trying to send aura sideways, but hold less aura than
				//the node I'm trying to send to, don't send any at all.
				return false;
			}
			*/
			int toEmit;
			
			if(fireAll) {
				//Try to emit all my aura.
				toEmit = amt;
			} else {
				//If I hold more aura than the node I'm sending to (plus one point)
				int auraDifference = storage.getTotalAura() - receiver.getStorage().getTotalAura();
				if(auraDifference > 1) {
					//Try to balance aura between myself and the other node.
					int auraAvg = MathHelper.ceil((storage.getTotalAura() + receiver.getStorage().getTotalAura()) / 2f);
					toEmit = storage.getTotalAura() - auraAvg;
				} else return false; //Don't send anything at all.
			}
			
			//Make sure I'm sending an actually reasonable amount of aura...
			toEmit = Utils.min(toEmit, amt, storage.getTotalAura(), receiver.getStorage().getRemainingSpace());
			
			if(toEmit != 0) {
				if(forReal) {
					storage.removeAura(type, toEmit);
					receiver.receiveAura(type, toEmit, this);
				}
				return true;
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
