package quaternary.halogen.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import quaternary.halogen.block.node.BlockNode;
import quaternary.halogen.block.plain.BlockRiftStone;

public class HaloBlocks {
	static final HaloBlock[] BLOCKS = {
		new BlockNode("node_white"),
		new BlockRiftStone("normal"),
		new BlockRiftStone("polished"),
		new BlockRiftStone("chiseled"),
	};
	
	public static void registerBlocks(IForgeRegistry<Block> reg) {
		for(HaloBlock b: BLOCKS) {
			reg.register(b);
			
			if(b.hasTileEntity(null)) {
				GameRegistry.registerTileEntity(b.getTileEntityClass(), b.getRegistryName().toString());
			}
		}
	}
	
	public static void registerItemBlocks(IForgeRegistry<Item> reg) {
		for(HaloBlock b: BLOCKS) {
			reg.register(b.getItemForm());
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerItemBlockModels() {
		for(HaloBlock b: BLOCKS) {
			setCMRL(b.getItemForm());
		}
	}
	
	@SideOnly(Side.CLIENT)
	private static void setCMRL(Item i) {
		ModelLoader.setCustomModelResourceLocation(i, 0, new ModelResourceLocation(i.getRegistryName(), "inventory"));
	}
}
