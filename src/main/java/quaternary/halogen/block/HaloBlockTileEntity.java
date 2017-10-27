package quaternary.halogen.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

//Thanks shadowfacts tutorial you're the best
public abstract class HaloBlockTileEntity<T extends TileEntity> extends HaloBlock {
	public HaloBlockTileEntity(String name, Material mat, EnumHaloBlockType type, AxisAlignedBB aabb) {
		super(name, mat, type, aabb);
	}
	
	public abstract Class<T> getTileEntityClass();
	
	@Override
	public boolean hasTileEntity(IBlockState blah) {
		return true;
	}
	
	@Override
	public abstract T createTileEntity(World world, IBlockState state);
}
