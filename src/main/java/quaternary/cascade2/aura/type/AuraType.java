package quaternary.cascade2.aura.type;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class AuraType {
	public abstract String getName();
	
	abstract int getCrystalDamage();
	
	@GameRegistry.ObjectHolder("halogen:aura_crystal")
	public static final Item AURA_CRYSTAL_ITEM = null;
	
	public ItemStack getCrystalItemStack() {
		return new ItemStack(AURA_CRYSTAL_ITEM, 1, getCrystalDamage());
	}
}