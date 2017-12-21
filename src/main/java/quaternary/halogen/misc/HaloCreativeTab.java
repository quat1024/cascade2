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
	
	@GameRegistry.ObjectHolder("halogen:aura_crystal_normal")
	public static final Item auraCrystalItem = null;
	
	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(auraCrystalItem, 1, 0);
	}
}
