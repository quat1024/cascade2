package quaternary.halogen.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import quaternary.halogen.Halogen;

import javax.annotation.Nullable;

/**
 * base class for halogen blocks.
 * Also simplifies some fullCube logic, etc (less dependent on the block material)
 */
public class HaloBlock extends Block {
	protected ItemBlock itemForm;
	public String name;
	
	boolean passable;
	
	public HaloBlock(String jeff, Material mat) {
		super(mat);
		
		name = jeff; //My name jeff
		
		fullBlock = true;
		
		setRegistryName(name);
		setUnlocalizedName(Halogen.MODID + "." + name);
		setCreativeTab(Halogen.CREATIVE_TAB);
	}
	
	//I know it's not usually a good pattern to make methods like "setNotThis" "setNotThat"
	//But since the "archetypal" block is a full block, I think it makes sense here.
	/** Mark this block as a non-full cube. */
	public HaloBlock setNonFullBlock() {
		fullBlock = false;
		translucent = true;
		setLightOpacity(0);
		return this;
	}
	
	/** Mark this block as one that can be pathfound through, etc */
	public HaloBlock setPassable() {
		passable = false;
		return this;
	}
	
	/** Return an Item that is associated with this HaloBlock.
	 * The default implementation creates an ItemBlock - override if something fancier is needed.*/
	public Item getItemForm() {
		if(itemForm == null) {
			itemForm = new ItemBlock(this);
			itemForm.setRegistryName(name);
		}
		return itemForm;
	}
	
	//A more sane default value ;)	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing facing) {
		return fullBlock ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
	}
	
	//Overrides to make some things less dependent on like... block materials
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
		return passable;
	}
	
	/** Override if using a tile entity. Called from HaloBlocks to register the tile entity. */
	@Nullable
	public Class getTileEntityClass() {
		return null;
	}
}
