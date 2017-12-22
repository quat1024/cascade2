package quaternary.halogen.aura.cap.impl;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import quaternary.halogen.aura.cap.*;
import quaternary.halogen.aura.type.AuraType;
import quaternary.halogen.aura.type.AuraTypes;

import java.util.EnumMap;

public class AuraEmitterCap implements IAuraEmitter {
	IAuraStorage storageCap;
	
	//todo: keep track of "aura bursts"
	
	public AuraEmitterCap(IAuraStorage storageCap) {
		this.storageCap = storageCap;
	}	
	
	@Override
	public void emitAuraTick(int amount, EnumMap<EnumFacing, IAuraReceiver> neighbors) {
		//Make sure I'm not sending more aura than I actually have
		amount = Math.min(amount, storageCap.getTotalContainedAura());
		
		//Early out if sending nothing
		if(amount == 0) return;
		
		//First: I can't send aura upwards at all, so just drop this one now.
		neighbors.remove(EnumFacing.UP);
		
		//Second: Since aura falls downwards, try to drop as much aura downwards as possible.
		if(neighbors.containsKey(EnumFacing.DOWN) /* and i'm not already sending anything downwards */) {
			IAuraReceiver downwardsReceiver = neighbors.get(EnumFacing.DOWN);
			
			int remainingReceiverSpace = downwardsReceiver.getRemainingAuraSpace();
			//try to send all of my aura downwards, but don't overfill it either
			int amountToSendDownwards = Math.min(amount, remainingReceiverSpace);
			emitAura(AuraTypes.NORMAL, amount, downwardsReceiver);
			amount -= amountToSendDownwards;
			
			neighbors.remove(EnumFacing.DOWN);
		}
		
		//Early out if there's no aura left.
		if(amount == 0) return;
		
		//Third: Evenly spread out the remaining aura sideways...
		//...while also trying to balance with the other nodes...
		//...while not "prioritizing" any one side over another.
		//Tricky.
		
		//...
	}
	
	public void emitAura(AuraType type, int amt, IAuraReceiver receiver) {
		storageCap.removeAura(type, amt);
		
		//Todo: Ideally I'd produce some sort of "aura burst" entity (or not-entity)
		//For now, though, just teleport the aura over there.
		receiver.receiveAura(type, amt);
	}
	
	@Override
	public NBTTagCompound writeNBT() {
		return new NBTTagCompound();
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt) {
		
	}
}
