package quaternary.halogen.block.plain;

import net.minecraft.block.material.Material;
import quaternary.halogen.block.HaloBlock;

public class BlockRiftStone extends HaloBlock {
	public BlockRiftStone(String variant) {
		super("rift_stone_" + variant, Material.ROCK);
	}
}
