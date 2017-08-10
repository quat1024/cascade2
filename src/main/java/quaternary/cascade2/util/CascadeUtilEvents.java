package quaternary.cascade2.util;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import quaternary.cascade2.Cascade;

public class CascadeUtilEvents {
	//Stoled from kit's smarthud
	@SideOnly(Side.CLIENT)
	public static long clientTicks;
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent e) {
		if(e.phase == TickEvent.Phase.END && !Minecraft.getMinecraft().isGamePaused()) {
			clientTicks++;
		}
	}
}
