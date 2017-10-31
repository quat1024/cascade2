package quaternary.halogen.aura.cap.impl.emitter;

import quaternary.halogen.aura.cap.IAuraStorage;

public class AuraDownwardsEmitterCap extends AuraEmitterCap {
	public AuraDownwardsEmitterCap(IAuraStorage storage) {
		super(storage);
	}
	
	@Override
	int getEmittedAura(IAuraStorage other, int amt) {
		return amt;
	}
}
