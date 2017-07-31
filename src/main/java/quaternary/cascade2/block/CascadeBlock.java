package quaternary.cascade2.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import quaternary.cascade2.Cascade;

public class CascadeBlock extends Block {
	
	ItemBlock itemForm;
	String name;
	
	public CascadeBlock(String jeff, Material mat) {
		super(mat);
		
		name = jeff; //My name jeff
		
		setRegistryName(name);
		setUnlocalizedName(name);
		
		setCreativeTab(Cascade.CREATIVE_TAB);
		
		itemForm = new ItemBlock(this);
		itemForm.setRegistryName(name); //TODO should I append _item?
		itemForm.setUnlocalizedName(name);
	}
	
	public ItemBlock getItemBlock() {
		return itemForm;
	}
	
	public void registerItemBlockModel() {
		//todo: handle data values
		Cascade.PROXY.registerItemModel(itemForm);
	}
}
