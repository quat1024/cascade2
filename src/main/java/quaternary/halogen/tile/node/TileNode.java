package quaternary.halogen.tile.node;
// Comment with space after the slashes to appease Nerxit.

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import quaternary.halogen.cap.AuraStorageCap;
import quaternary.halogen.util.DisgustingNumbers;

public class TileNode extends TileEntity implements ITickable {
	AuraStorageCap storageCap = new AuraStorageCap(DisgustingNumbers.NODE_MAX_AURA);
	
	@Override
	public void update() {
		
	}
}
