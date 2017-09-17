package quaternary.cascade2.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import quaternary.cascade2.Cascade;
import quaternary.cascade2.aura.type.AuraType;
import quaternary.cascade2.aura.type.AuraTypeRegistry;
import quaternary.cascade2.aura.crystal.IAuraCrystal;

public class ItemAuraCrystal extends CascadeItem implements IAuraCrystal {
	//TODO move this int to aura system class
	public static final int MAX_AURA_COLORS = 8;
	
	public ItemAuraCrystal() {
		super("aura_crystal");
		
		setHasSubtypes(true);
		setMaxDamage(0);
	}
	
	//FIXME: This is a dumb place to put this?
	public AuraType getAuraType(ItemStack stack) {
		return AuraTypeRegistry.fromString("normal");
	}
	
	public int getAuraContained(ItemStack stack) {
		return 50;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		//TODO actual aura names
		return super.getUnlocalizedName() + "." + stack.getMetadata();
	}
	
	//TODO: add functionality for this in ModItems, maybe.
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> stacks) {
		if(isInCreativeTab(tab)) {
			for(int i = 0; i < MAX_AURA_COLORS; i++) {
				stacks.add(new ItemStack(this, 1, i));
			}
		}
	}
	
	@Override
	public void registerSubtypeModels() {
		for(int i = 0; i < MAX_AURA_COLORS; i++) {
			//todo: actual aura names
			Cascade.PROXY.registerItemModelWithSuffix(this, i, "type" + i);
		}
	}
}
