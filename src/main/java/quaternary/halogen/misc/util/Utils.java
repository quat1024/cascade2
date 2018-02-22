package quaternary.halogen.misc.util;

import com.google.common.base.Preconditions;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class Utils {
	//Varargs min function
	public static int min(int... numbers) {
		int i = Integer.MAX_VALUE;
		for(int j : numbers) {
			if(j < i) i = j;
		}
		return i;
	}
	
	//Check that an itemstack isn't something silly like null or air
	public static void checkItemStack(ItemStack stack, String what) {
		Preconditions.checkNotNull(stack, what + " stack can't be null");
		Preconditions.checkNotNull(stack.getItem(), what + " item can't be null");
		Preconditions.checkArgument(stack.getItem() != Items.AIR, what + " item can't be air");
	}
}
