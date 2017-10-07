package quaternary.cascade2.item;

import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;
import quaternary.cascade2.Halogen;

public class ModItems {
	static HaloItem[] items = {
					new ItemAuraCrystal()
	};
	
	public static void registerItems(IForgeRegistry<Item> reg) {
		reg.registerAll(items);
	}
	
	public static void registerItemModels() {
		//todo: handle data values properly instead of just assuming there aren't any
		//for example aura crystal items should DEFFO have a dv
		for(HaloItem i : items) {
			if(i.getHasSubtypes()) {
				i.registerSubtypeModels();
			} else {
				Halogen.PROXY.registerItemModel(i);
			}
		}
	}
}
