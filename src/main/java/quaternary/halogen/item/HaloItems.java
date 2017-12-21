package quaternary.halogen.item;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import quaternary.halogen.aura.type.AuraTypes;

public class HaloItems {
	static HaloItem[] items = {
		new ItemAuraCrystal(AuraTypes.NORMAL),
		new ItemRift(),
		new HaloItem("rift_dust")
	};
	
	public static void registerItems(IForgeRegistry<Item> reg) {
		reg.registerAll(items);
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerItemModels() {
		//todo: handle data values properly instead of just assuming there aren't any
		//HELLO PAST QUAT
		//this isn't coming out for 1.12 anyways so let's just party like it's 1.13
		for(HaloItem i : items) {
			ModelLoader.setCustomModelResourceLocation(i, 0,
			new ModelResourceLocation(i.getRegistryName(), "inventory"));
		}
	}
}
