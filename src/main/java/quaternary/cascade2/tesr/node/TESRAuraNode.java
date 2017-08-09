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
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.5 + x,0.5 + y,0.5 + z);
		
		GlStateManager.glLineWidth(3f);
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(BEAM_FX_LOC);
		
		for(Map.Entry<EnumFacing,BlockPos> pair : te.getConnectionMap().entrySet()) {
			//I don't want to render connections 2 times from 2 nodes
			if(pair.getKey().getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) {
				BlockPos otherPos = pair.getValue();
				
				//TODO: fancy render
				
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
