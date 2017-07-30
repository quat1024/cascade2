package quaternary.cascade2.block;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;
import quaternary.cascade2.Cascade;
import quaternary.cascade2.block.node.BlockAuraNode;

public class ModBlocks {
	//hey hey because of the new objectholder thing
	//i don't have to keep easily-accessible public
	//instances for all my blocks. nnnnnnice.
	public static final CascadeBlock[] blocks = {
					new BlockAuraNode("node_white")
	};
	
	public static void registerBlocks(IForgeRegistry<Block> reg) {
		reg.registerAll(blocks);
	}
	
	public static void registerItemBlocks(IForgeRegistry<Item> reg) {
		for(CascadeBlock b: blocks) {
			reg.register(b.getItemBlock());
		}
	}
	
	public static void registerItemBlockModels() {
		for(CascadeBlock b: blocks) {
			b.registerItemBlockModel();
		}
	}
}
