package quaternary.cascade2.cap.aura;

import quaternary.cascade2.aura.type.AuraType;
import quaternary.cascade2.cap.ISaveLoadCapability;

import java.util.concurrent.ConcurrentHashMap;

/** Something that can hold aura. */
public interface IAuraHolder extends ISaveLoadCapability<IAuraHolder> {
	ConcurrentHashMap<AuraType, Integer> getAuraMap();
	void replaceAuraMap(ConcurrentHashMap<AuraType, Integer> newMap);
	int getMaximumTotalAura();
	int getContainedAura(AuraType type);
	int getAllContainedAura();
	boolean canAddAura(AuraType type, int amount);
	boolean canRemoveAura(AuraType type, int amount);
	void addAura(AuraType type, int amount);
	void removeAura(AuraType type, int amount);
	void clearAura();
	void setAura(AuraType type, int amount);
}
