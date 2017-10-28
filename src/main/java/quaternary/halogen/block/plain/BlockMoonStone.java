package quaternary.halogen.block.plain;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import quaternary.halogen.block.EnumHaloBlockType;
import quaternary.halogen.block.HaloBlock;

import javax.annotation.Nonnull;

public class BlockMoonStone extends HaloBlock {
	static final PropertyEnum<EnumMoonstoneStates> MOONSTONE_STATES = PropertyEnum.create("type", BlockMoonStone.EnumMoonstoneStates.class);
	
	public BlockMoonStone() {
		super("moon_stone", Material.ROCK, EnumHaloBlockType.FULLCUBE, Block.FULL_BLOCK_AABB);
		
		setDefaultState(blockState.getBaseState().withProperty(MOONSTONE_STATES, EnumMoonstoneStates.NORMAL));
	}
	
	//blockstate stuff
	
	@Nonnull
	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, MOONSTONE_STATES);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return blockState.getBaseState().withProperty(MOONSTONE_STATES, EnumMoonstoneStates.values()[meta]);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(MOONSTONE_STATES).ordinal();
	}
	
	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
		for(int i = 0; i < EnumMoonstoneStates.values().length; i++) {
			items.add(new ItemStack(this, 1, i));
		}
	}
	
	enum EnumMoonstoneStates implements IStringSerializable {
		NORMAL("normal"),
		POLISHED("polished"),
		CHISELED("chiseled");
		
		private String name;
		
		EnumMoonstoneStates(String name_) {
			name = name_;
		}
		
		public String getName() { return name; }
	}
}
