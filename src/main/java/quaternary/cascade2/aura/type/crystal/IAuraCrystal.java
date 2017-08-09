package quaternary.cascade2.aura.type.crystal;

import net.minecraft.item.ItemStack;
import quaternary.cascade2.aura.type.AuraType;

//An item implementing this can be eaten by an aura node.
//TODO: caps
public interface IAuraCrystal {
	public AuraType getAuraType(ItemStack stack);
	
	public int getAuraContained(ItemStack stack);
}
