package quaternary.halogen.block.plain;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import quaternary.halogen.block.EnumHaloBlockType;
import quaternary.halogen.block.HaloBlock;

public class BlockRiftStone extends HaloBlock {
	String variant;
	
	public BlockRiftStone(String variant_) {
		super("rift_stone_" + variant_, Material.ROCK, EnumHaloBlockType.FULLCUBE, Block.FULL_BLOCK_AABB);
		
		variant = variant_;
		
		//setDefaultState(blockState.getBaseState().withProperty(MOONSTONE_STATES, EnumMoonstoneStates.NORMAL));
		
		//itemForm = new ItemMultiTexture(this, this, stack -> EnumMoonstoneStates.nameFromID(stack.getMetadata()));
		//itemForm.setRegistryName(name);
	}
}
