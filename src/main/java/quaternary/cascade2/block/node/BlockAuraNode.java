package quaternary.cascade2.block.node;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import quaternary.cascade2.block.CascadeBlock;

public class BlockAuraNode extends CascadeBlock {
	
	static final AxisAlignedBB AABB = new AxisAlignedBB(
					1/4d, 1/4d, 1/4d, 
					3/4d, 3/4d, 3/4d);
	
	public BlockAuraNode(String jeff) {
		super(jeff, Material.ROCK); //Todo: real material choice
		
		setLightOpacity(0);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return AABB;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
}
