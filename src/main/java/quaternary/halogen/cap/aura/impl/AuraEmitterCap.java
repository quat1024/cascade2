package quaternary.halogen.cap.aura.impl;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
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
	
	//It's a mess!
	@Override
	public boolean canEmitAura(AuraType type, int amt, EnumFacing whichWay, @Nonnull IAuraReceiver receiver) {
		return doEmitAura(type, amt, whichWay, receiver, false);
	}
	
	@Override
	public void emitAura(AuraType type, int amt, EnumFacing whichWay, @Nonnull IAuraReceiver receiver) {
		doEmitAura(type, amt, whichWay, receiver, true);
	}
	
	private boolean doEmitAura(AuraType type, int amt, EnumFacing whichWay, @Nonnull IAuraReceiver receiver, boolean forReal) {
		if(whichWay == EnumFacing.UP) return false; //This node can't send aura upwards
		
		if(isEligible() && receiver.isEligible()) {
			int auraDifference = storage.getTotalAura() - receiver.getStorage().getTotalAura();
			
			int toEmit;
			if(whichWay == EnumFacing.DOWN) {
				//Unconditionally try to empty all aura downwards.
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
			
			//clamp aura to reasonable levels
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
