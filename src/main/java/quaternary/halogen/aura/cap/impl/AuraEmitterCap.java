package quaternary.halogen.aura.cap.impl;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import quaternary.halogen.Halogen;
import quaternary.halogen.aura.cap.*;
import quaternary.halogen.aura.type.AuraType;
import quaternary.halogen.aura.type.AuraTypes;

import java.util.EnumMap;
import java.util.Map;

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
		
		if(amount == 0 || neighbors.isEmpty()) return;
		
		//Third: Evenly spread out the remaining aura sideways...
		//...while also trying to balance with the other nodes...
		//...while not "prioritizing" any one side over another.
		
		for(Map.Entry<EnumFacing, IAuraReceiver> entry : neighbors.entrySet()) {
			EnumFacing whichWay = entry.getKey();
			IAuraReceiver rec = entry.getValue();
			
			//if the other has more aura, don't send anything that way
			//(also ignore differences of only 1 aura point)
			if(rec.getTotalContainedAura() + 1 >= storageCap.getTotalContainedAura()) neighbors.remove(whichWay);
		}
		
		if(neighbors.isEmpty()) return;
		
		int receiverCount = neighbors.size();
		
		//make sure it's always possible to send the same amount of aura to all sides
		amount -= amount % receiverCount;
		int amountToSend = amount / receiverCount;
		
		//check if it's okay to just blindly send the whole requested amount
		boolean ok = true;
		for(Map.Entry<EnumFacing, IAuraReceiver> entry : neighbors.entrySet()) {
			ok &= entry.getValue().getRemainingAuraSpace() > amountToSend;
			if(!ok) break;
		}
		if(ok) {
			for(Map.Entry<EnumFacing, IAuraReceiver> entry : neighbors.entrySet()) {
				emitAura(AuraTypes.NORMAL, amountToSend, entry.getValue());
			}
		} else {
			Halogen.LOGGER.info("More complex logic needed to handle this case?");
		}
		
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
