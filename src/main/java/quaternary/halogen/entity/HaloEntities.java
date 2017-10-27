package quaternary.halogen.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import quaternary.halogen.Halogen;
import quaternary.halogen.client.entityrenderer.RenderNothing;

public class HaloEntities {
	public static final String THROWN_RIFT_NAME = "thrown_rift";
	public static final String RIFT_NAME = "rift";
	
	public static void registerEntities() {
		//thrown rift
		reg(THROWN_RIFT_NAME, EntityThrownRift.class, 64, 5,  true);
		//the rift itself
		//Todo: Make this entity send its own packets, maybe, because it doesn't update regularly.
		reg(RIFT_NAME,        EntityRift.class,       96, 10, true);
	}
	
	private static int id = 0;
	/** quick wrapper to make the registration process a bit cleaner shh */
	private static void reg(String name, Class c, int trackingRange, int trackingFreq, boolean velocityPackets) {
		EntityRegistry.registerModEntity(resify(name), c, prefixify(name), id++, Halogen.INSTANCE, trackingRange, trackingFreq, velocityPackets);
	}
	
	@GameRegistry.ObjectHolder("halogen:rift")
	public static final Item riftItem = null;
	
	@SideOnly(Side.CLIENT)
	public static void registerEntityRenderers() {		
		RenderingRegistry.registerEntityRenderingHandler(EntityThrownRift.class, rm -> new RenderSnowball<>(rm, riftItem, Minecraft.getMinecraft().getRenderItem()));
		RenderingRegistry.registerEntityRenderingHandler(EntityRift.class, RenderNothing::new);
	}
	
	/** The internal resloc this entity is stored under.
	 * Also the name for command blocks.
	 * Apparently those can include colons now! Who knew. */
	private static ResourceLocation resify(String in) {
		return new ResourceLocation(Halogen.MODID , in);
	}
	
	/** The name used to look up localization strings. */
	private static String prefixify(String in) {
		return Halogen.MODID + in; //no colon
	}
}
