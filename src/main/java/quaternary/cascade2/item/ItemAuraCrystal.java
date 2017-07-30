package quaternary.cascade2.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import quaternary.cascade2.Cascade;

public class ItemAuraCrystal extends CascadeItem {
	//TODO move this to aura system class
	public static final int MAX_AURA_COLORS = 8;
	
	public ItemAuraCrystal() {
		super("aura_crystal");
		
		setHasSubtypes(true);
		setMaxDamage(0);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		//TODO when aura colors are introduced, use their *name* for localization
		//instead of this number index boringness
		return super.getUnlocalizedName() + "." + stack.getMetadata();
	}
	
	//TODO: add functionality for this in ModItems, maybe.
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> stacks) {
		if(isInCreativeTab(tab)) {
			for(int i=0; i < MAX_AURA_COLORS; i++) {
				stacks.add(new ItemStack(this, 1, i));
			}
		}
	}
}
