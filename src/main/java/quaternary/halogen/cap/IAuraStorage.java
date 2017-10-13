package quaternary.halogen.cap;

import quaternary.halogen.aura.type.AuraType;

public interface IAuraStorage extends ISaveLoadCapability {
	boolean canAddAura(AuraType type, int amount);
	boolean canRemoveAura(AuraType type, int amount);
	
	void addAura(AuraType type, int amount, boolean safe);
	void removeAura(AuraType type, int amount, boolean safe);
	
	int getAura(AuraType type);
}
