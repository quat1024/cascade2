package quaternary.halogen.aura.cap;

import net.minecraft.util.EnumFacing;
import quaternary.halogen.cap.ISaveLoadCapability;

import java.util.EnumMap;

public interface IAuraEmitter extends ISaveLoadCapability {
	//Halp i'm sure there's a better name for this.
	void emitAuraTick(int totalAmount, EnumMap<EnumFacing, IAuraReceiver> neighbors);
}
