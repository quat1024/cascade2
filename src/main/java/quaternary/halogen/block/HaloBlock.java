package quaternary.halogen.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import quaternary.halogen.Halogen;

public class HaloBlock extends Block {
	
	ItemBlock itemForm;
	String name;
	
	private boolean solid;
	
	public HaloBlock(String jeff, Material mat, boolean solid_) {
		super(mat);
		
		name = jeff; //My name jeff
		
		setRegistryName(name);
		setUnlocalizedName(Halogen.MODID + "." + name);
		
		setCreativeTab(Halogen.CREATIVE_TAB);
		
		itemForm = new ItemBlock(this);
		itemForm.setRegistryName(name);
		
		solid = solid_;
	}
	
	public ItemBlock getItemBlock() {
		return itemForm;
	}
	
	public void registerItemBlockModel() {
		//todo: handle data values
		Halogen.PROXY.registerItemModel(itemForm);
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing facing) {
		return solid ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return solid;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return solid;
	}
}
