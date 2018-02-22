package quaternary.halogen.item;

import net.minecraft.item.ItemStack;
import quaternary.halogen.aura.type.AuraType;
import quaternary.halogen.misc.DisgustingNumbers;

public class ItemAuraCrystal extends HaloItem {
	public final AuraType type;
	
	public ItemAuraCrystal(AuraType type) {
		super("aura_crystal_" + type.getName());
		
		this.type = type;
	}
	
	public int getContainedAura(ItemStack stack) {
		return DisgustingNumbers.AURA_CRYSTAL_CONTAINED_AURA;
	}
}
