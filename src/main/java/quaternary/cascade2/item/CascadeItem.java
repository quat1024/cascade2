package quaternary.cascade2.item;

import net.minecraft.item.Item;
import quaternary.cascade2.Cascade;

public class CascadeItem extends Item {
	
	String name;
	
	public CascadeItem(String jeff) {
		name = jeff;
		
		setRegistryName(name);
		setUnlocalizedName(name);
		setCreativeTab(Cascade.CREATIVE_TAB);
	}
}
