package quaternary.halogen.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import quaternary.halogen.Halogen;

/** base class for halogen blocks. 
 * Also simplifies some fullCube logic, etc (less dependent on the block material) */
public class HaloBlock extends Block {
	
	protected ItemBlock itemForm;
	public String name;
	
	//the reason this has a default value: super() has to be first for somefuckingreason in java
	//and Block's constructor looks at things like isOpaqueCube which is null if this is too
	private EnumHaloBlockType type = EnumHaloBlockType.FULLCUBE;
	private AxisAlignedBB aabb;
	
	public HaloBlock(String jeff, Material mat, EnumHaloBlockType type_, AxisAlignedBB aabb_) {
		super(mat);
		
		name = jeff; //My name jeff
		aabb = aabb_;
		type = type_;
		setLightOpacity(type.OPACITY);
		
		translucent = !type.OPAQUE_FULL_CUBE;
		fullBlock = type.OPAQUE_FULL_CUBE;
		
		setRegistryName(name);
		setUnlocalizedName(Halogen.MODID + "." + name);
		setCreativeTab(Halogen.CREATIVE_TAB);
		
		itemForm = new ItemBlock(this);
		itemForm.setRegistryName(name);
	}
	
	//Convenience
	HaloBlock(String jeff, Material mat) {
		this(jeff, mat, EnumHaloBlockType.FULLCUBE, Block.FULL_BLOCK_AABB);
	}
	
	public ItemBlock getItemBlock() {
		return itemForm;
	}
	
	void registerItemBlockModel() {
		//todo: handle data values
		ModelLoader.setCustomModelResourceLocation(itemForm, 0, 
			new ModelResourceLocation(itemForm.getRegistryName(), "inventory"));
	}
	
	//Begin convenience overrides
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing facing) {
		return type.OPAQUE_FULL_CUBE? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return fullBlock;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return fullBlock;
	}
	
	@Override
	public boolean isNormalCube(IBlockState blah, IBlockAccess blahhh, BlockPos blahblah) {
		return fullBlock;
	}
	
	@Override
	public boolean isPassable(IBlockAccess blah, BlockPos blahblah) {
		return type.PASSABLE;
	}
	
	//is this even used? /shrug
	@Override
	public boolean isCollidable() {
		return !type.PASSABLE;
	}
	
	public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
		return type.OPACITY;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return aabb;
	}
}
