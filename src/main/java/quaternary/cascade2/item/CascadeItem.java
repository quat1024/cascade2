package quaternary.cascade2.item;

import net.minecraft.item.Item;
import quaternary.cascade2.Cascade;
import quaternary.cascade2.aura.type.crystal.IAuraCrystal;

public class CascadeItem extends Item {
	
	String name;
	
	public CascadeItem(String jeff) {
		name = jeff;
		
		setRegistryName(name);
		setUnlocalizedName(name);
		setCreativeTab(Cascade.CREATIVE_TAB);
	}
	
	//Called in ModItems, override iff using subtypes
	public void registerSubtypeModels() {
		
	}
}
