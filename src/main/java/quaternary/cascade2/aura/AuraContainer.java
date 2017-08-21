package quaternary.cascade2.aura;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import quaternary.cascade2.aura.type.AuraTypeRegistry;
import quaternary.cascade2.aura.type.AuraType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//Describes the amount of aura in a specific aura holder device thing.
public class AuraContainer {
	private ConcurrentHashMap<AuraType, Integer> auraStorage;
	int maxTotal;
	
	public AuraContainer(int maxTotal_) {
		maxTotal = maxTotal_;
		auraStorage = new ConcurrentHashMap<>();
	}
	
	//Returns whether the aura adding operation was successful.
	public boolean addAuraOfType(AuraType type, int amtToAdd) {
		if(canAddAuraAmount(amtToAdd)) {
			auraStorage.put(type, getType(type) + amtToAdd);
			return true;
		}
		return false;
	}
	
	public boolean canAddAuraAmount(int amtToTest) {
		return getTotalAuraContained() + amtToTest <= maxTotal;
	}
	
	public int getType(AuraType type) {
		return auraStorage.getOrDefault(type, 0);
	}
	
	public int getMaxAura() { return maxTotal; }
	
	public int getTotalAuraContained() {
		//There's still no nice way to do this in Java :/
		int sum = 0;
		for(int i : auraStorage.values()) sum += i;
		return sum;
	}
	
	public void clear() {
		auraStorage.clear();
	}
	
	private static final String TYPE_KEY = "type";
	private static final String AMOUNT_KEY = "amount";
	
	public NBTTagList writeToNBTList() {
		NBTTagList list = new NBTTagList();
		if(!auraStorage.isEmpty()) {
			for(Map.Entry<AuraType, Integer> entry : auraStorage.entrySet()) {
				NBTTagCompound comp = new NBTTagCompound();
				comp.setString(TYPE_KEY, entry.getKey().getName());
				comp.setInteger(AMOUNT_KEY, entry.getValue());
				list.appendTag(comp);
			}
		}
		return list;
	}
	
	public AuraContainer readFromNBTList(NBTTagList list) {
		auraStorage = new ConcurrentHashMap<>();
		for(int i=0; i < list.tagCount(); i++) {
			NBTTagCompound entry = list.getCompoundTagAt(i);
			AuraType type = AuraTypeRegistry.fromString(entry.getString(TYPE_KEY));
			int amount = entry.getInteger(AMOUNT_KEY);
			auraStorage.put(type, amount);
		}
		
		return this;
	}
}
