package quaternary.halogen.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
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
public class BlockBase extends Block {
	protected ItemBlock itemForm;
	public String name;
	
	boolean passable;
	
	public BlockBase(String jeff, Material mat, MapColor color) {
		super(mat, color);
		
		name = jeff; //My name jeff
		
		fullBlock = true;
		
		setRegistryName(Halogen.MODID, name);
		setUnlocalizedName(Halogen.MODID + "." + name);
		setCreativeTab(Halogen.CREATIVE_TAB);
	}
	
	//Flags?
	
	public BlockBase setNonFullBlock() {
		fullBlock = false;
		translucent = true;
		setLightOpacity(0);
		return this;
	}
	
	public BlockBase setPassable() {
		passable = false;
		return this;
	}
	
	//Items
	
	public boolean hasItemForm() {
		return true;
	}
	
	public Item getItemForm() {
		if(itemForm == null) {
			itemForm = new ItemBlock(this);
			itemForm.setRegistryName(name);
		}
		return itemForm;
	}
	
	//Tile entity stuff
	public boolean hasTileEntity() {
		return false;
	}
	
	@Nullable
	public Class getTileEntityClass() {
		return null;
	}
	
	//Useful overrides, I guess
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing facing) {
		return fullBlock ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
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
		return passable;
	}
}
