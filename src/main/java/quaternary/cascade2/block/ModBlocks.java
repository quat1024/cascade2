package quaternary.cascade2.block;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import quaternary.cascade2.block.node.BlockAuraNode;

public class ModBlocks {
	public static final HaloBlock[] blocks = {
					new BlockAuraNode("node_white")
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
