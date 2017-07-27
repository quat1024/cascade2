package quaternary.cascade2.misc;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import quaternary.cascade2.Cascade;

public class CascadeCreativeTab extends CreativeTabs {
	public CascadeCreativeTab() {
		super(Cascade.MODID);
	}
	
	//ok real talk this objectholder stuff is good shit
	@GameRegistry.ObjectHolder("cascade2:aura_crystal")
	public static final Item auraCrystalItem = null;
	
	public ItemStack getTabIconItem() {
		return new ItemStack(auraCrystalItem);
	}
}
