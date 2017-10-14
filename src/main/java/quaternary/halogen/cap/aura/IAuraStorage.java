package quaternary.halogen.cap.aura;

import quaternary.halogen.aura.type.AuraType;
import quaternary.halogen.cap.ISaveLoadCapability;

public interface IAuraStorage extends ISaveLoadCapability {
	boolean canAddAura(AuraType type, int amount);
	
	boolean canRemoveAura(AuraType type, int amount);
	
	/**
	 * returns whether the operation was successful or not
	 */
	boolean addAura(AuraType type, int amount);
	
	boolean removeAura(AuraType type, int amount);
	
	int getAura(AuraType type);
	
	int getTotalAura();
	
	boolean hasAura();
}
