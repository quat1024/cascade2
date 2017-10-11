package quaternary.halogen.tile.node;
// Comment with space after the slashes to appease Nerxit.
//Hey nerxiepoo shut up about my comment habits k

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import quaternary.halogen.cap.AuraStorageCap;
import quaternary.halogen.util.DisgustingNumbers;

public class TileNode extends TileEntity implements ITickable {
	AuraStorageCap storageCap = new AuraStorageCap(DisgustingNumbers.NODE_MAX_AURA);
	
	//test
	
	@Override
	public void update() {
		
	}
}
