package quaternary.halogen.tile;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;
import quaternary.halogen.tile.node.TileNode;

public class HaloTiles {
	//todo SPAGHETI
	@GameRegistry.ObjectHolder("halogen:node_white")
	public static final Block blockNode = null;
	
	public static void registerTiles() {
		GameRegistry.registerTileEntity(TileNode.class, "halogen:node_white");
	}
}
