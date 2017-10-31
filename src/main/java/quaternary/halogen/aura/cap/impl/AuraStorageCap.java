package quaternary.halogen.aura.cap.impl;

import net.minecraft.nbt.*;
import quaternary.halogen.aura.cap.IAuraStorage;
import quaternary.halogen.aura.type.AuraType;
import quaternary.halogen.aura.type.AuraTypes;
import quaternary.halogen.cap.ISaveLoadCapability;

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
		return getTotalAura() + amount <= max;
	}
	
	@Override
	public boolean addAura(AuraType type, int amount) {
		if(!canAddAura(type, amount)) return false;
		
		if(storageMap.containsKey(type)) {
			storageMap.put(type, storageMap.get(type) + amount);
		} else {
			storageMap.put(type, amount);
		}
		return true;
	}
	
	@Override
	public boolean removeAura(AuraType type, int amount) {
		if(!canRemoveAura(type, amount)) return false;
		
		storageMap.put(type, storageMap.get(type) - amount);
		return true;
	}
	
	@Override
	public boolean canRemoveAura(AuraType type, int amount) {
		return storageMap.containsKey(type) && storageMap.get(type) >= amount;
	}
	
	@Override
	public boolean hasAura() {
		if(storageMap.isEmpty()) return false;
		return getTotalAura() > 0;
	}
	
	@Override
	public int getAura(AuraType type) {
		return storageMap.getOrDefault(type, 0);
	}
	
	@Override
	public int getMaximumAura() {
		return max;
	}
	
	@Override
	public int getTotalAura() {
		int blah = 0;
		for(Map.Entry<AuraType, Integer> entry : storageMap.entrySet()) {
			blah += entry.getValue();
		}
		return blah;
	}
	
	@Override
	public boolean hasSpace() {
		return getRemainingSpace() != 0;
	}
	
	@Override
	public int getRemainingSpace() {
		return max - getTotalAura();
	}
	
	//////////////
	
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
