package quaternary.cascade2.block.node;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import quaternary.cascade2.block.CascadeBlockTileEntity;
import quaternary.cascade2.tile.node.TileEntityAuraNode;

public abstract class BlockAuraNode extends CascadeBlockTileEntity<TileEntityAuraNode> {
	static final AxisAlignedBB AABB = new AxisAlignedBB(
					1 / 4d, 1 / 4d, 1 / 4d,
					3 / 4d, 3 / 4d, 3 / 4d);
	
	public BlockAuraNode(String jeff) {
		super(jeff, Material.ROCK); //Todo: real material choice
		
		setLightOpacity(0);
	}
	
	//debug only
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(hand == EnumHand.OFF_HAND) return false;
		TileEntityAuraNode myTE = getCastedTileEntity(world, pos);
		if(myTE != null) {
			myTE.onActivated(world, player);
		}
		return false;
	}
	
	//messages to tile entity
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		getCastedTileEntity(worldIn, pos).onPlaceBlock();
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		getCastedTileEntity(worldIn, pos).onBreakBlock();
		super.breakBlock(worldIn, pos, state);
	}
	
	//tile entity setup
	@Override
	public Class<TileEntityAuraNode> getTileEntityClass() {
		return TileEntityAuraNode.class;
	}
	
	@Override
	public TileEntityAuraNode createTileEntity(World world, IBlockState state) {
		return new TileEntityAuraNode();
	}
	
	//block props
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
	
	@Override
	public boolean isPassable(IBlockAccess blah, BlockPos blahblah) {
		return false;
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing facing) {
		return BlockFaceShape.UNDEFINED;
	}
}
