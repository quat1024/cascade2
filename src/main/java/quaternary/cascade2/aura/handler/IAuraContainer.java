package quaternary.cascade2.aura.handler;

import quaternary.cascade2.aura.type.AuraType;

import java.util.concurrent.ConcurrentHashMap;

public interface IAuraContainer {
	ConcurrentHashMap<AuraType, Integer> getAuraMap();
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
