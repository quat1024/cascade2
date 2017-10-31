package quaternary.halogen.aura.cap;

import quaternary.halogen.aura.type.AuraType;
import quaternary.halogen.cap.ISaveLoadCapability;

import javax.annotation.Nonnull;

public interface IAuraEmitter extends ISaveLoadCapability {
	IAuraStorage getStorage();
	
	void setEligible(boolean canEmit);
	
	boolean isEligible();
	
	boolean canEmitAura(AuraType type, int amt, @Nonnull IAuraReceiver reciever);
	
	void emitAura(AuraType type, int amt, @Nonnull IAuraReceiver receiver);
}