package quaternary.halogen.recipe;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.Optional;

@GameRegistry.ObjectHolder("halogen")
public class HaloRiftRecipes {
	private static ArrayList<RiftRecipe> recipes = new ArrayList<>();
	
	@GameRegistry.ObjectHolder("rift_dust")
	public static final Item MOON_DUST = null;
	
	@GameRegistry.ObjectHolder("rift_stone_normal")
	public static final Item MOON_STONE = null;
	
	//called in init
	public static void registerRecipes() {
		recipes.add(new RiftRecipe(Items.REDSTONE, MOON_DUST));
		recipes.add(new RiftRecipe(Item.getItemFromBlock(Blocks.STONE), MOON_STONE));
	}
	
	public static Optional<ItemStack> getOutput(ItemStack in) {
		for(RiftRecipe i : recipes) {
			Optional<ItemStack> output = i.getOutput(in);
			if(output.isPresent()) return output;
		}
		
		return Optional.empty();
	}
}
