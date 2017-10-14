package quaternary.halogen.cap.aura;

import quaternary.halogen.aura.type.AuraType;
import quaternary.halogen.cap.ISaveLoadCapability;

public interface IAuraReceiver extends ISaveLoadCapability {
	IAuraStorage getStorage();
	
	void setEligible(boolean canReceive);
	
	boolean isEligible();
	
	boolean canReceiveAura(AuraType type, int amt);
	
	void receiveAura(AuraType type, int amt, IAuraEmitter emitter);
}
