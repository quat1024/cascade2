package quaternary.halogen.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import quaternary.halogen.Halogen;
import quaternary.halogen.entity.EntityThrownRift;

@GameRegistry.ObjectHolder(Halogen.MODID)
public class ClientProxy extends CommonProxy {
	@Override
	public void registerItemModel(Item i) {
		ModelLoader.setCustomModelResourceLocation(i, 0,
						new ModelResourceLocation(i.getRegistryName(), "inventory"));
	}
	
	@Override
	public void registerItemModelWithSuffix(Item i, int dv, String name) {
		ModelLoader.setCustomModelResourceLocation(i, dv,
						new ModelResourceLocation(i.getRegistryName() + "_" + name, "inventory"));
	}
	
	@GameRegistry.ObjectHolder("rift")
	public static final Item riftItem = null;
	
	@Override
	public void registerEntityRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(EntityThrownRift.class, renderManager -> new RenderSnowball<>(renderManager, riftItem, Minecraft.getMinecraft().getRenderItem()));
	}
}
