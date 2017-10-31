package quaternary.halogen.aura.cap;

import net.minecraft.util.EnumFacing;
import quaternary.halogen.aura.type.AuraType;
import quaternary.halogen.cap.ISaveLoadCapability;

import javax.annotation.Nonnull;

public interface IAuraEmitter extends ISaveLoadCapability {
	IAuraStorage getStorage();
	
	void setEligible(boolean canEmit);
	
	boolean isEligible();
	
	boolean canEmitAura(AuraType type, int amt, EnumFacing whichWay, @Nonnull IAuraReceiver reciever);
	
	void emitAura(AuraType type, int amt, EnumFacing whichWay, @Nonnull IAuraReceiver receiver);
}
