package quaternary.halogen.item;

import net.minecraft.item.Item;
import quaternary.halogen.Halogen;

public class HaloItem extends Item {
	
	String name;
	
	public HaloItem(String jeff) {
		name = jeff;
		
		setRegistryName(name);
		setUnlocalizedName(name);
		setCreativeTab(Halogen.CREATIVE_TAB);
	}
	
	//Called in ModItems, override iff using subtypes
	public void registerSubtypeModels() {
		
	}
}
