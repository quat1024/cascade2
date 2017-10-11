package quaternary.halogen.util;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;

public class RenderUtils {
	public static void point(BufferBuilder b, double x, double y, double z, float u, float v, EnumFacing way) {
		Vec3i norm = way.getDirectionVec();
		b.pos(x, y, z).tex(u, v).color(255, 255, 255, 255).normal(norm.getX(), norm.getY(), norm.getZ()).endVertex();
	}
}
