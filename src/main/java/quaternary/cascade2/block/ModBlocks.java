package quaternary.cascade2.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;
import quaternary.cascade2.Cascade;
import quaternary.cascade2.block.node.BlockAuraNode;

public class ModBlocks {
	public static final BlockAuraNode bepis = new BlockAuraNode("node_white");
	
	public static void registerBlocks(IForgeRegistry<Block> reg) {
		reg.register(bepis);
	}
	
	public static void registerItemBlocks(IForgeRegistry<Item> reg) {
		reg.register(bepis.getItemBlock());
	}
	
	public static void registerItemBlockModels() {
		Cascade.PROXY.registerItemModel(bepis.getItemBlock(), 0);
	}
}
