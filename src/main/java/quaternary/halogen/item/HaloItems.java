package quaternary.halogen.item;

import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;
import quaternary.halogen.Halogen;

public class HaloItems {
	static HaloItem[] items = {
					new ItemAuraCrystal(),
					new ItemRift(),
					new HaloItem("moon_dust")
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
