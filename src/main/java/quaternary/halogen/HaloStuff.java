package quaternary.halogen;

import quaternary.halogen.aura.type.AuraTypes;
import quaternary.halogen.block.HaloBlock;
import quaternary.halogen.block.node.BlockNode;
import quaternary.halogen.block.plain.BlockMoonStone;
import quaternary.halogen.item.*;

public class HaloStuff {
	public static final HaloItem[] ITEMS = {
		new ItemAuraCrystal(AuraTypes.NORMAL),
		new ItemRift(),
		new HaloItem("moon_dust")
	};
	
	public static final HaloBlock[] BLOCKS = {
		new BlockNode("node_white"),
		new BlockMoonStone()
	};
}
