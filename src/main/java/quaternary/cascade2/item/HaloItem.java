package quaternary.cascade2.item;

import net.minecraft.item.Item;
import quaternary.cascade2.Halogen;

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
