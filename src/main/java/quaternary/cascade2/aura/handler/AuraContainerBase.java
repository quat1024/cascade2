package quaternary.cascade2.aura.handler;

import quaternary.cascade2.aura.type.AuraType;
import java.util.concurrent.ConcurrentHashMap;

public class AuraContainerBase implements IAuraContainer {
	private ConcurrentHashMap<AuraType, Integer> auraStorage;
	int maxTotal;
	
	public AuraContainerBase(int maxTotal_) {
		maxTotal = maxTotal_;
	}
	
	//Getters
	@Override
	public ConcurrentHashMap<AuraType, Integer> getAuraMap() {
		return auraStorage;
	}
	
	@Override
	public int getMaximumTotalAura() {
		return maxTotal;
	}
	
	@Override
	public int getContainedAura(AuraType type) {
		return auraStorage.getOrDefault(type, 0);
	}
	
	@Override
	public int getAllContainedAura() {
		int sum=0;
		for(int i : auraStorage.values()) sum += i;
		return sum;
	}
	
	@Override
	public boolean canAddAura(AuraType type, int amount) {
		//note that I don't care about the type in this impl
		return getAllContainedAura() + amount <= maxTotal;
	}
	
	@Override
	public boolean canRemoveAura(AuraType type, int amount) {
		return getContainedAura(type) >= amount && getAllContainedAura() - amount >= 0;
	}
	
	@Override
	public void addAura(AuraType type, int amount) {
		if(canAddAura(type, amount)) {
			auraStorage.put(type, auraStorage.getOrDefault(type, 0) + amount);
		}
	}
	
	@Override
	public void removeAura(AuraType type, int amount) {
		if(canRemoveAura(type, amount)) {
			auraStorage.put(type, auraStorage.get(type) - amount);
		}
	}	
	
	//Utility
	@Override
	public void clearAura() {
		auraStorage.clear();
	}
	
	@Override
	public void setAura(AuraType type, int amount) {
		auraStorage.put(type, amount);
	}
}
