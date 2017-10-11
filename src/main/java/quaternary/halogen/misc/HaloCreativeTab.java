package quaternary.halogen.misc;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import quaternary.halogen.Halogen;

public class HaloCreativeTab extends CreativeTabs {
	public HaloCreativeTab() {
		super(Halogen.MODID);
	}
	
	@GameRegistry.ObjectHolder("cascade2:aura_crystal")
	public static final Item auraCrystalItem = null;
	
	public ItemStack getTabIconItem() {
		return new ItemStack(auraCrystalItem);
	}
}
