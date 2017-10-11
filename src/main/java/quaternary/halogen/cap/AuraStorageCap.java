package quaternary.halogen.cap;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import quaternary.halogen.aura.type.AuraType;
import quaternary.halogen.aura.type.AuraTypes;

import java.util.HashMap;
import java.util.Map;

public class AuraStorageCap implements IAuraStorage, ISaveLoadCapability {
	private HashMap<AuraType, Integer> storageMap = new HashMap<>();
	private int max;
	
	public AuraStorageCap(int max) {
		this.max = max;
	}
	
	@Override
	public boolean canAddAura(AuraType type, int amount) {
		return sumAura() + amount <= max;
	}
	
	@Override
	public boolean canRemoveAura(AuraType type, int amount) {
		return storageMap.containsKey(type) && storageMap.get(type) >= amount;
	}
	
	@Override
	public void addAura(AuraType type, int amount, boolean safe) {
		if(safe && !canAddAura(type, amount)) return;
		
		if(storageMap.containsKey(type)) {
			storageMap.put(type, storageMap.get(type) + amount);
		} else {
			storageMap.put(type, amount);
		}
	}
	
	@Override
	public void removeAura(AuraType type, int amount, boolean safe) {
		if(safe && !canRemoveAura(type, amount)) return;
		
		storageMap.put(type, storageMap.get(type) - amount);
	}
	
	private int sumAura() {
		int blah = 0;
		for(Map.Entry<AuraType, Integer> entry : storageMap.entrySet()) {
			blah += entry.getValue();
		}
		return blah;
	}
	
	@Override
	public NBTBase writeNBT() {
		NBTTagList list = new NBTTagList();
		for(Map.Entry<AuraType, Integer> entry : storageMap.entrySet()) {
			NBTTagCompound e = new NBTTagCompound();
			e.setString("Type", entry.getKey().getName());
			e.setInteger("Amt", entry.getValue());
			list.appendTag(e);
		}
		return list;
	}
	
	@Override
	public void readNBT(NBTBase nbt) {
		if(nbt instanceof NBTTagList) {
			storageMap.clear();
			NBTTagList list = (NBTTagList) nbt;
			for(int i = 0; i < list.tagCount(); i++) {
				NBTTagCompound e = (NBTTagCompound) list.get(i);
				storageMap.put(AuraTypes.fromString(e.getString("Type")), e.getInteger("Amt")
				);
			}
		} else {
			throw new IllegalArgumentException("Tried to decode an " + nbt.getClass() + " but I need a NBTTagList, what am I supposed to do with this, please tell me.");
		}
	}
}
