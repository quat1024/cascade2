package quaternary.halogen.block.plain;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import quaternary.halogen.block.BlockBase;

public class BlockVoidStone extends BlockBase {
	public BlockVoidStone(String variant) {
		super("voidtouched_stone_" + variant, Material.ROCK, MapColor.CYAN_STAINED_HARDENED_CLAY);
	}
}
