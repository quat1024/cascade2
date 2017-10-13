package quaternary.halogen.cap.aura;

import quaternary.halogen.aura.type.AuraType;
import quaternary.halogen.cap.ISaveLoadCapability;

public interface IAuraEmitter extends ISaveLoadCapability {
	IAuraStorage getStorage();
	
	void setEligible(boolean canEmit);
	boolean isEligible();
	
	boolean canEmitAura(AuraType type, int amt, IAuraReceiver reciever);
	void emitAura(AuraType type, int amt, IAuraReceiver receiver);
}
