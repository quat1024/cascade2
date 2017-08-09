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
	@Override
	public final void render(TileEntityAuraNode te, double x, double y, double z, float partialTicks, int destroyStage, float partial) {
		BlockPos myPos = te.getPos();
		
		Tessellator tes = Tessellator.getInstance();
		BufferBuilder buffer = tes.getBuffer();
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.5 + x,0.5 + y,0.5 + z);
		
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("minecraft:textures/blocks/stone.png"));
		
		GlStateManager.glLineWidth(3f);
		
		for(Map.Entry<EnumFacing,BlockPos> pair : te.getConnectionMap().entrySet()) {
			//I don't want to render connections 2 times from 2 nodes
			if(pair.getKey().getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) {
				BlockPos otherPos = pair.getValue();
				
				buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
				buffer.pos(0,0,0).color(255,0,255,255).endVertex();
				buffer.pos(otherPos.getX()-myPos.getX(),otherPos.getY()-myPos.getY(),otherPos.getZ()-myPos.getZ()).color(255,0,255,255).endVertex();
				tes.draw();
			}
		}
		
		
		GlStateManager.popMatrix();
	}
	
	//todo: delet this
	public boolean isGlobalRenderer(TileEntityAuraNode bla) {
		return true;
	}
}
