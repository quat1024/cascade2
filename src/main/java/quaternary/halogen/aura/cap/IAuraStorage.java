package quaternary.halogen.aura.cap;

import quaternary.halogen.aura.type.AuraType;
import quaternary.halogen.cap.ISaveLoadCapability;

public interface IAuraStorage extends ISaveLoadCapability {
	/** How much aura can this aura container hold? */
	int getMaxAura();
	
	/** How much aura is in this container right now? */
	int getTotalContainedAura();
	
	/** Insert a particular amount of aura. */
	void addAura(AuraType type, int amt);
	
	/** Remove a particular amount of aura. */
	void removeAura(AuraType type, int amt);
	
	/** Get how much of a particular type of aura is in this node. */
	int getAura(AuraType type);
}
