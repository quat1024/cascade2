package quaternary.halogen.aura.cap.impl;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import quaternary.halogen.Halogen;
import quaternary.halogen.aura.cap.IAuraStorage;
import quaternary.halogen.aura.type.AuraType;
import quaternary.halogen.aura.type.AuraTypes;

import java.util.HashMap;
import java.util.Map;

public class AuraStorageCap implements IAuraStorage {
	private int max;
	
	HashMap<AuraType, Integer> containedAuraMap = new HashMap<>();
	
	public AuraStorageCap(int max) {
		this.max = max;
	}
	
	@Override
	public int getMaxAura() {
		return max;
	}
	
	@Override
	public int getTotalContainedAura() {
		int runningSum = 0;
		for(Integer i: containedAuraMap.values()) {
			runningSum += i;
		}
		return runningSum;
	}
	
	@Override
	public void addAura(AuraType type, int amt) {
		if(getTotalContainedAura() + amt > max) {
			Halogen.LOGGER.info("Tried to overfill an AuraStorageCap, voiding extra"); //TODO remove debug print ;)
			amt = getTotalContainedAura() - max;
		}
		
		containedAuraMap.put(type, containedAuraMap.getOrDefault(type, 0) + amt);
	}
	
	@Override
	public void removeAura(AuraType type, int amt) {
		if(getAura(type) - amt < 0) {
			Halogen.LOGGER.info("Tried to remove more aura than I have"); //TODO remove debug print ;)
			containedAuraMap.remove(type);
			return;
		}
		
		//remove the key from the map if there is none of that type of aura in me
		int newAmt = containedAuraMap.get(type) - amt;
		if(newAmt == 0) {
			containedAuraMap.remove(type);
		} else {
			containedAuraMap.put(type, containedAuraMap.get(type) - amt);
		}
	}
	
	@Override
	public int getAura(AuraType type) {
		return containedAuraMap.getOrDefault(type, 0);
	}
	
	private static final String AURA_LIST_KEY = "Aura";
	private static final String TYPE_KEY = "Type";
	private static final String AMOUNT_KEY = "Amt";
	
	@Override
	public NBTTagCompound writeNBT() {
		NBTTagList list = new NBTTagList();
		for(Map.Entry<AuraType, Integer> e : containedAuraMap.entrySet()) {
			NBTTagCompound entry = new NBTTagCompound();
			entry.setString(TYPE_KEY, e.getKey().getName());
			entry.setInteger(AMOUNT_KEY, e.getValue());
			list.appendTag(entry);
		}
		
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag(AURA_LIST_KEY, nbt);
		return nbt;
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt) {
		NBTTagList list = nbt.getTagList(AURA_LIST_KEY, 10);
		containedAuraMap.clear();
		for(int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound entry = (NBTTagCompound) list.get(i);
			AuraType type = AuraTypes.fromName(entry.getString(TYPE_KEY));
			int amt = entry.getInteger(AMOUNT_KEY);
			
			containedAuraMap.put(type, amt);
		}
	}
}
