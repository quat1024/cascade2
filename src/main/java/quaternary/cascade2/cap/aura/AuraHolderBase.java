package quaternary.cascade2.cap.aura;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import quaternary.cascade2.aura.type.AuraType;
import quaternary.cascade2.aura.type.AuraTypeRegistry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AuraHolderBase implements IAuraHolder {
	private ConcurrentHashMap<AuraType, Integer> auraStorage;
	private int maxTotal;
	
	public AuraHolderBase(int maxTotal_) {
		maxTotal = maxTotal_;
	}
	
	//Idk getters and setters bullshit i should sort these.
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
		//note that I don't care about the type
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
	
	@Override
	public ConcurrentHashMap<AuraType, Integer> getAuraMap() {
		return auraStorage;
	}
	
	@Override
	public void replaceAuraMap(ConcurrentHashMap<AuraType, Integer> newMap) {
		auraStorage = newMap;
	}
	
	//NBT
	public NBTBase writeNBT() {
		NBTTagList list = new NBTTagList();
		if(!auraStorage.isEmpty()) {
			for(Map.Entry<AuraType, Integer> entry : auraStorage.entrySet()) {
				NBTTagCompound comp = new NBTTagCompound();
				comp.setString("type", entry.getKey().getName());
				comp.setInteger("amount", entry.getValue());
				list.appendTag(comp);
			}
		}
		return list;
	}
	
	public void readNBT(NBTBase nbt) {
		NBTTagList list = (NBTTagList) nbt;
		clearAura();
		for(int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound entry = list.getCompoundTagAt(i);
			AuraType type = AuraTypeRegistry.fromString(entry.getString("type"));
			int amount = entry.getInteger("amount");
			auraStorage.put(type, amount);
		}
	}
}
