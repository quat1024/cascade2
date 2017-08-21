package quaternary.cascade2.util;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CascadeUtils {
	//Copy of the one from MathHelper, but not clientside only (WHY IS IT SIDEONLY???)
	public static int fastFloor(double blah) {
		return (int)(blah + 1024.0D) - 1024;
	}
	
	public static int blockPosDistance(BlockPos a, BlockPos b) {
		return fastFloor(a.getDistance(b.getX(), b.getY(), b.getZ()));
	}
	
	public static BlockPos blockPosFromArray(int[] array) {
		return new BlockPos(array[0], array[1], array[2]);
	}
	
	public static TileEntity getLoadedTileEntity(World world, BlockPos pos) {
		if(world.isBlockLoaded(pos)) return world.getTileEntity(pos);
		else return null;
	}
}
