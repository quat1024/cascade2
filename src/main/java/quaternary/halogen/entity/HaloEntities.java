package quaternary.halogen.entity;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import quaternary.halogen.Halogen;

public class HaloEntities {
	public static final String RIFT_NAME = "rift";
	
	public static void registerEntities() {
		//rift
		reg(RIFT_NAME, EntityThrownRift.class, 64, 5, true);
	}
	
	private static int id = 0;
	/** quick wrapper to make the registration process a bit cleaner shh */
	private static void reg(String name, Class c, int trackingRange, int trackingFreq, boolean velocityPackets) {
		EntityRegistry.registerModEntity(resify(name), c, prefixify(name), id++, Halogen.INSTANCE, trackingRange, trackingFreq, velocityPackets);
	}
	
	/** The internal resloc this entity is stored under. */
	private static ResourceLocation resify(String in) {
		return new ResourceLocation(Halogen.MODID , in);
	}
	
	/** The name used in command blocks, to look up localization strings, pretty much everything else. 
	 * It's important that this string doesn't have any funny characters in it, like :, or else
	 * it becomes impossible to target my entities with a command block. @e[type=halogen:rift]
	 * does not parse. */
	private static String prefixify(String in) {
		return Halogen.MODID + in; //no colon
	}
}
