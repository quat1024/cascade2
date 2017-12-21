package quaternary.halogen.aura.cap.impl;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import quaternary.halogen.aura.cap.*;

import java.util.Map;

public class AuraEmitterCap implements IAuraEmitter {
	IAuraStorage storageCap;
	public AuraEmitterCap(IAuraStorage storageCap) {
		this.storageCap = storageCap;
	}	
	
	void emit(int amt, Map<EnumFacing, IAuraReceiver> neighbors) {
		
	}
	
	@Override
	public NBTBase writeNBT() {
		return null;
	}
	
	@Override
	public void readNBT(NBTBase nbt) {
		
	}
}
