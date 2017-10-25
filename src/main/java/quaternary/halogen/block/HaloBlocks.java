package quaternary.halogen.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import quaternary.halogen.block.node.BlockNode;

public class HaloBlocks {
	public static final HaloBlock[] blocks = {
					new BlockNode("node_white"),
					new HaloBlock("moon_stone", Material.ROCK),
					new HaloBlock("polished_moon_stone", Material.ROCK)
	};
	
	public static void registerBlocks(IForgeRegistry<Block> reg) {
		for(HaloBlock b : blocks) {
			reg.register(b);
			
			if(b instanceof HaloBlockTileEntity) {
				HaloBlockTileEntity bt = (HaloBlockTileEntity) b;
				GameRegistry.registerTileEntity(bt.getTileEntityClass(), bt.getRegistryName().toString());
			}
		}
	}
	
	public static void registerItemBlocks(IForgeRegistry<Item> reg) {
		for(HaloBlock b : blocks) {
			reg.register(b.getItemBlock());
		}
	}
	
	public static void registerItemBlockModels() {
		for(HaloBlock b : blocks) {
			b.registerItemBlockModel();
		}
	}
}
