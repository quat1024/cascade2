package quaternary.halogen.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import quaternary.halogen.Halogen;

public class HaloBlock extends Block {
	
	ItemBlock itemForm;
	String name;
	
	public HaloBlock(String jeff, Material mat) {
		super(mat);
		
		name = jeff; //My name jeff
		
		setRegistryName(name);
		setUnlocalizedName(name);
		
		setCreativeTab(Halogen.CREATIVE_TAB);
		
		itemForm = new ItemBlock(this);
		itemForm.setRegistryName(name);
		itemForm.setUnlocalizedName(name);
	}
	
	public ItemBlock getItemBlock() {
		return itemForm;
	}
	
	public void registerItemBlockModel() {
		//todo: handle data values
		Halogen.PROXY.registerItemModel(itemForm);
	}
}
