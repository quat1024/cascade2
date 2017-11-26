package quaternary.halogen.item;

import quaternary.halogen.aura.type.AuraType;

public class ItemAuraCrystal extends HaloItem {
	AuraType type;
	//todo: Maybe pass in an auratype instead of this thing?
	public ItemAuraCrystal(AuraType type_) {
		super("aura_crystal_" + type_.getName());
		
		type = type_;
	}
	
	//TODO: add functionality for this in ModItems, maybe.
	/*
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
			//Halogen.PROXY.registerItemModelWithSuffix(this, i, "type" + i);
			ModelLoader.setCustomModelResourceLocation(this, i,
				new ModelResourceLocation(this.getRegistryName() + "_type" + i, "inventory"));
		}
	}
	*/
}
