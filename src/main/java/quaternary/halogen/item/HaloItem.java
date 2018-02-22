package quaternary.halogen.item;

import net.minecraft.item.Item;
import quaternary.halogen.Halogen;

public class HaloItem extends Item {
	String name;
	
	public HaloItem(String jeff) {
		name = jeff;
		
		setRegistryName(Halogen.MODID, name);
		setUnlocalizedName(Halogen.MODID + "." + name);
		setCreativeTab(Halogen.CREATIVE_TAB);
	}
}
