package quaternary.cascade2.misc;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import quaternary.cascade2.Halogen;

public class CascadeCreativeTab extends CreativeTabs {
	public CascadeCreativeTab() {
		super(Halogen.MODID);
	}
	
	@GameRegistry.ObjectHolder("cascade2:aura_crystal")
	public static final Item auraCrystalItem = null;
	
	public ItemStack getTabIconItem() {
		return new ItemStack(auraCrystalItem);
	}
}
