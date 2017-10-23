package quaternary.halogen.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.opengl.GL11;
import quaternary.halogen.tile.node.TileNode;

@Mod.EventBusSubscriber(Side.CLIENT)
public class HUD {
	@SubscribeEvent
	public static void renderHUD(RenderGameOverlayEvent.Post e) {
		if(e.getType() != RenderGameOverlayEvent.ElementType.ALL) return;		
		
		Minecraft mc = Minecraft.getMinecraft();
		RayTraceResult trace = mc.objectMouseOver;
		Profiler p = mc.mcProfiler;
		ScaledResolution res = e.getResolution();
		
		p.startSection("halogen-hud");
		
		if(trace != null && trace.typeOfHit == RayTraceResult.Type.BLOCK) {
			TileEntity te = mc.world.getTileEntity(trace.getBlockPos());
			if(te instanceof TileNode) {
				int centerX = res.getScaledWidth() / 2;
				int centerY = res.getScaledHeight() / 2;
				
				Tessellator t = Tessellator.getInstance();
				BufferBuilder b = t.getBuffer();
				
				GlStateManager.enableBlend();
				GlStateManager.disableLighting();
				GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GlStateManager.disableTexture2D();
				GlStateManager.colorMask(true, true, true, true);
				GlStateManager.depthMask(true);
				GL11.glStencilMask(0);
				GL11.glStencilFunc(GL11.GL_EQUAL, 1, 0xff);
				
				
				int bepis = 2;
				
				b.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
				b.pos(centerX - bepis, centerY - bepis, 0).color(1f, 1f, 1f, 0f).endVertex();
				b.pos(centerX + bepis, centerY - bepis, 0).color(1f, 1f, 1f, 0f).endVertex();
				b.pos(centerX + bepis, centerY + bepis, 0).color(1f, 1f, 1f, 0f).endVertex();
				b.pos(centerX - bepis, centerY + bepis, 0).color(1f, 1f, 1f, 0f).endVertex();
				
				GlStateManager.disableBlend();
				GlStateManager.enableTexture2D();
				GL11.glDisable(GL11.GL_STENCIL_TEST);
				
				t.draw();
			}
		}
		
		GlStateManager.color(1f, 1f, 1f, 1f);
		
		p.endSection();
	}
}
