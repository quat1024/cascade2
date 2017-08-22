package quaternary.cascade2.block;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import quaternary.cascade2.block.node.BlockAuraNodeNormal;

public class ModBlocks {
	public static final CascadeBlock[] blocks = {
					new BlockAuraNodeNormal()
	};
	
	public static void registerBlocks(IForgeRegistry<Block> reg) {
		for(CascadeBlock b : blocks) {
			reg.register(b);
			
			if(b instanceof CascadeBlockTileEntity) {
				CascadeBlockTileEntity bt = (CascadeBlockTileEntity) b;
				GameRegistry.registerTileEntity(bt.getTileEntityClass(), bt.getRegistryName().toString());
			}
		}
	}
	
	public static void registerItemBlocks(IForgeRegistry<Item> reg) {
		for(CascadeBlock b : blocks) {
			reg.register(b.getItemBlock());
		}
	}
	
	public static void registerItemBlockModels() {
		for(CascadeBlock b : blocks) {
			b.registerItemBlockModel();
		}
	}
}
