package quaternary.cascade2.tesr.node;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;
import quaternary.cascade2.cap.aura.connection.ConnectionData;
import quaternary.cascade2.tile.node.TileEntityAuraNode;
import quaternary.cascade2.util.CascadeRenderUtils;

import java.util.Map;

//todo: fasttesr
public class TESRAuraNode extends TileEntitySpecialRenderer<TileEntityAuraNode> {
	public static final ResourceLocation BEAM_FX_LOC = new ResourceLocation("cascade2:textures/etc/node_connector.png");
	
	@Override
	public final void render(TileEntityAuraNode te, double x, double y, double z, float partialTicks, int destroyStage, float partial) {
		BlockPos myPos = te.getPos();
		
		Tessellator tes = Tessellator.getInstance();
		BufferBuilder buffer = tes.getBuffer();
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(BEAM_FX_LOC);
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.enableBlend();
		
		//TODO revisit this
		//float kindaRandomOffset = (myPos.getX()*36.3f + myPos.getY()*163.7f + myPos.getZ()*12.9f)%360f;
		
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
		
		for(Map.Entry<EnumFacing, ConnectionData> pair : te.getActiveConnections().entrySet()) {
			EnumFacing whichWay = pair.getKey();
			//I don't want to render connections 2 times from 2 nodes
			if(whichWay.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) {
				BlockPos otherPos = pair.getValue().position;
				float x2 = otherPos.getX() - myPos.getX() + .5f + THICC;
				float y2 = otherPos.getY() - myPos.getY() + .5f + THICC;
				float z2 = otherPos.getZ() - myPos.getZ() + .5f + THICC;
				box(buffer, .5f - THICC, .5f - THICC, .5f - THICC, x2, y2, z2);
			}
		}
		
		tes.draw();
		
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}
	
	private static final float THICC = 0.125f; //radius
	
	private void box(BufferBuilder b, double x1, double y1, double z1, double x2, double y2, double z2) {
		//Top (positive Y)
		CascadeRenderUtils.point(b, x1, y2, z1, 0, 0, EnumFacing.UP);
		CascadeRenderUtils.point(b, x1, y2, z2, 0, 1, EnumFacing.UP);
		CascadeRenderUtils.point(b, x2, y2, z2, 1, 1, EnumFacing.UP);
		CascadeRenderUtils.point(b, x2, y2, z1, 1, 0, EnumFacing.UP);
		
		//North (negative Z)
		CascadeRenderUtils.point(b, x1, y1, z1, 0, 0, EnumFacing.NORTH);
		CascadeRenderUtils.point(b, x1, y2, z1, 0, 1, EnumFacing.NORTH);
		CascadeRenderUtils.point(b, x2, y2, z1, 1, 1, EnumFacing.NORTH);
		CascadeRenderUtils.point(b, x2, y1, z1, 1, 0, EnumFacing.NORTH);
		
		//East (positive X)
		CascadeRenderUtils.point(b, x2, y1, z1, 0, 0, EnumFacing.EAST);
		CascadeRenderUtils.point(b, x2, y2, z1, 1, 0, EnumFacing.EAST);
		CascadeRenderUtils.point(b, x2, y2, z2, 1, 1, EnumFacing.EAST);
		CascadeRenderUtils.point(b, x2, y1, z2, 0, 1, EnumFacing.EAST);
		
		//South (positive Z)
		CascadeRenderUtils.point(b, x1, y1, z2, 0, 0, EnumFacing.SOUTH);
		CascadeRenderUtils.point(b, x2, y1, z2, 1, 0, EnumFacing.SOUTH);
		CascadeRenderUtils.point(b, x2, y2, z2, 1, 1, EnumFacing.SOUTH);
		CascadeRenderUtils.point(b, x1, y2, z2, 0, 1, EnumFacing.SOUTH);
		
		//West (negative X)
		CascadeRenderUtils.point(b, x1, y1, z1, 0, 0, EnumFacing.WEST);
		CascadeRenderUtils.point(b, x1, y1, z2, 0, 1, EnumFacing.WEST);
		CascadeRenderUtils.point(b, x1, y2, z2, 1, 1, EnumFacing.WEST);
		CascadeRenderUtils.point(b, x1, y2, z1, 1, 0, EnumFacing.WEST);
		
		//Down (negative Y)
		CascadeRenderUtils.point(b, x1, y1, z1, 0, 0, EnumFacing.DOWN);
		CascadeRenderUtils.point(b, x2, y1, z1, 1, 0, EnumFacing.DOWN);
		CascadeRenderUtils.point(b, x2, y1, z2, 1, 1, EnumFacing.DOWN);
		CascadeRenderUtils.point(b, x1, y1, z2, 0, 1, EnumFacing.DOWN);
	}
	
	public boolean isGlobalRenderer(TileEntityAuraNode bla) {
		return true;
	}
}
