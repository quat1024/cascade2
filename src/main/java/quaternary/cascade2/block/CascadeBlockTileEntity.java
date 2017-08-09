package quaternary.cascade2.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

//Thanks shadowfacts tutorial you're the best
public abstract class CascadeBlockTileEntity<T extends TileEntity> extends CascadeBlock {
	public CascadeBlockTileEntity(String name, Material mat) {
		super(name, mat);
	}
	
	public abstract Class<T> getTileEntityClass();
	
	//Convenience function so we don't need to cast calls to world.getTileEntity 
	public T getCastedTileEntity(IBlockAccess world, BlockPos pos) {
		return (T) world.getTileEntity(pos);
	}
	
	@Override
	public boolean hasTileEntity(IBlockState blah) {
		return true;
	}
	
	@Override
	public abstract T createTileEntity(World world, IBlockState state);
}
