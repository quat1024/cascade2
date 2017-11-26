package quaternary.halogen.block.node;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import quaternary.halogen.block.EnumHaloBlockType;
import quaternary.halogen.block.HaloBlock;
import quaternary.halogen.tile.node.TileNode;

public class BlockNode extends HaloBlock {
	static final AxisAlignedBB AABB = new AxisAlignedBB(
					1 / 4d, 1 / 4d, 1 / 4d,
					3 / 4d, 3 / 4d, 3 / 4d);
	
	public BlockNode(String jeff) {
		super(jeff, Material.ROCK, EnumHaloBlockType.NONFULL_SOLID, AABB);
	}
	
	//messages to tile entity
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		//((TileEntityAuraNode) world.getTileEntity(pos)).onPlaceBlock();
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		//((TileEntityAuraNode) world.getTileEntity(pos)).onBreakBlock();
		super.breakBlock(world, pos, state);
	}
	
	@Override
	public TileNode createTileEntity(World world, IBlockState state) {
		return new TileNode();
	}
	
	//block props
	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}
	
	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		return ((TileNode) world.getTileEntity(pos)).getComparatorLevel();
	}
}
