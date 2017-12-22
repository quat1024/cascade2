package quaternary.halogen.aura.cap.impl;

import net.minecraft.nbt.NBTTagCompound;
import quaternary.halogen.aura.cap.IAuraReceiver;
import quaternary.halogen.aura.cap.IAuraStorage;
import quaternary.halogen.aura.type.AuraType;

public class AuraReceiverCap implements IAuraReceiver {
	final IAuraStorage storageCap;
	boolean eligible = true;
	static final String ELIGIBLE_KEY = "EligibleReceiver";
	
	public AuraReceiverCap(IAuraStorage storageCap) {
		this.storageCap = storageCap;
	}
	
	@Override
	public boolean isEligible() {
		return eligible;
	}
	
	@Override
	public void setEligible(boolean eligible) {
		this.eligible = eligible;
	}
	
	@Override
	public int getTotalContainedAura() {
		return storageCap.getTotalContainedAura();
	}
	
	@Override
	public int getRemainingAuraSpace() {
		return storageCap.getMaxAura() - storageCap.getTotalContainedAura();
	}
	
	@Override
	public void receiveAura(AuraType type, int amt) {
		storageCap.addAura(type, amt);
	}
	
	@Override
	public NBTTagCompound writeNBT() {
		NBTTagCompound comp = new NBTTagCompound();
		comp.setBoolean(ELIGIBLE_KEY, eligible);
		return comp;
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt) {
		eligible = nbt.getBoolean(ELIGIBLE_KEY);
	}
}
