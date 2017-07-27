package quaternary.cascade2.proxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerItemModel(Item i, int dv) {
		ModelLoader.setCustomModelResourceLocation(i, 0,
						new ModelResourceLocation(i.getRegistryName(), "inventory"));
	}
}
