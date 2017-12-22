package quaternary.halogen.aura.cap;

import quaternary.halogen.aura.type.AuraType;
import quaternary.halogen.cap.ISaveLoadCapability;

public interface IAuraReceiver extends ISaveLoadCapability {
	/** Should aura emitters think about sending new aura bursts to me? */
	boolean isEligible();
	void setEligible(boolean eligible);
	
	/** How much aura does this item have? 
	 * Usually this delegates to an IAuraStorage. */
	int getTotalContainedAura();
	
	/** How much space is left in this item for aura?
	 * Usually this delegates to an IAuraStorage. */
	int getRemainingAuraSpace();
	
	/** Receive an amount of a particular type of aura.
	 * Ideally, destroy excess aura. */
	void receiveAura(AuraType type, int amt);
}
