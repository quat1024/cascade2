package quaternary.halogen.aura.cap.impl.emitter;

import quaternary.halogen.aura.cap.IAuraStorage;

public class AuraSidewaysEmitterCap extends AuraEmitterCap {
	public AuraSidewaysEmitterCap(IAuraStorage storage) {
		super(storage);
	}
	
	@Override
	int getEmittedAura(IAuraStorage other, int amt) {
		if(storage.getTotalAura() > other.getTotalAura()) {
			int auraAverage = (storage.getTotalAura() + other.getTotalAura()) / 2;
			return storage.getTotalAura() - auraAverage;
		} else return 0;
	}
}
