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
import quaternary.cascade2.tile.node.TileEntityAuraNode;

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
		GlStateManager.translate(0.5 + x,0.5 + y,0.5 + z);
		GlStateManager.enableBlend();
		
		//TODO revisit this
		//float kindaRandomOffset = (myPos.getX()*36.3f + myPos.getY()*163.7f + myPos.getZ()*12.9f)%360f;
		
		for(Map.Entry<EnumFacing,BlockPos> pair : te.getConnectionMap().entrySet()) {
			EnumFacing whichWay = pair.getKey();
			//I don't want to render connections 2 times from 2 nodes
			if(whichWay.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) {
				BlockPos otherPos = pair.getValue();
				
				GlStateManager.pushMatrix();
				
				int distance;
				if(whichWay == EnumFacing.EAST) { //positive X
					distance = otherPos.getX() - myPos.getX();
				} else if (whichWay == EnumFacing.UP) { //positive Y
					distance = otherPos.getY() - myPos.getY();
					GlStateManager.rotate(90f, 0f, 0f, 1f);
				} else { //positive Z
					distance = otherPos.getZ() - myPos.getZ();
					GlStateManager.rotate(90f, 0f, -1f, 0f);
				}
				
				buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
				box(buffer, distance);
				
				GlStateManager.popMatrix();
				
				tes.draw();
			}
		}
		
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}
	
	private static final float THICC = 0.13f; //radius
	
	private void box(BufferBuilder b, int len) {
		//FIXME: Quit repeating yourself you buffoon, and make this method less crappy
		//FIXME: uvs are fucked still (it's like, a rectangle, whatt?????)
		
		//Top
		b.pos(0, THICC, THICC)    .tex(0, 0).color(255, 255, 255, 255).normal(0, 1, 0).endVertex();
		b.pos(len, THICC, THICC)  .tex(len*2, 0).color(255, 255, 255, 255).normal(0, 1, 0).endVertex();
		b.pos(len, THICC, -THICC) .tex(len*2, 1).color(255, 255, 255, 255).normal(0, 1, 0).endVertex();
		b.pos(0, THICC, -THICC)   .tex(0, 1).color(255, 255, 255, 255).normal(0, 1, 0).endVertex();
		
		//Bottom (drawn the other way)
		b.pos(0, -THICC, -THICC)  .tex(0, 1).color(255, 255, 255, 255).normal(0, -1, 0).endVertex();
		b.pos(len, -THICC, -THICC).tex(len*2, 1).color(255, 255, 255, 255).normal(0, -1, 0).endVertex();
		b.pos(len, -THICC, THICC) .tex(len*2, 0).color(255, 255, 255, 255).normal(0, -1, 0).endVertex();
		b.pos(0, -THICC, THICC)   .tex(0, 0).color(255, 255, 255, 255).normal(0, -1, 0).endVertex();
		
		//One side (drawn the other way)
		b.pos(0, -THICC, THICC)   .tex(0, 1).color(255, 255, 255, 255).normal(0, 0, 1).endVertex();
		b.pos(len, -THICC, THICC) .tex(len*2, 1).color(255, 255, 255, 255).normal(0, 0, 1).endVertex();
		b.pos(len, THICC, THICC)  .tex(len*2, 0).color(255, 255, 255, 255).normal(0, 0, 1).endVertex();
		b.pos(0, THICC, THICC)    .tex(0, 0).color(255, 255, 255, 255).normal(0, 0, 1).endVertex();
		
		//The other
		b.pos(0, THICC, -THICC)   .tex(0, 0).color(255, 255, 255, 255).normal(0, 0, -1).endVertex();
		b.pos(len, THICC, -THICC) .tex(len*2, 0).color(255, 255, 255, 255).normal(0, 0, -1).endVertex();
		b.pos(len, -THICC, -THICC).tex(len*2, 1).color(255, 255, 255, 255).normal(0, 0, -1).endVertex();
		b.pos(0, -THICC, -THICC)  .tex(0, 1).color(255, 255, 255, 255).normal(0, 0, -1).endVertex();
	}
	
	public boolean isGlobalRenderer(TileEntityAuraNode bla) {
		return true;
	}
}
