package quaternary.halogen;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import quaternary.halogen.block.HaloBlocks;
import quaternary.halogen.cap.HaloCaps;
import quaternary.halogen.entity.HaloEntities;
import quaternary.halogen.item.HaloItems;
import quaternary.halogen.misc.HaloCreativeTab;
import quaternary.halogen.proxy.CommonProxy;

@Mod(modid = Halogen.MODID, name = Halogen.NAME, version = Halogen.VERSION)
public class Halogen {
	public static final String MODID = "halogen";
	public static final String NAME = "Halogen";
	public static final String VERSION = "0";
	
	@Mod.Instance
	public static Halogen INSTANCE = new Halogen();
	
	@SidedProxy(clientSide = "quaternary.halogen.proxy.ClientProxy",
					serverSide = "quaternary.halogen.proxy.CommonProxy")
	public static CommonProxy PROXY;
	
	public static final HaloCreativeTab CREATIVE_TAB = new HaloCreativeTab();
	public static final Logger LOGGER = LogManager.getLogger(MODID);
	
	@Mod.EventHandler
	@SuppressWarnings("unused")
	public void preinit(FMLPreInitializationEvent e) {
		//PROXY.registerTESRs();
		
		HaloCaps.registerCaps();
		HaloEntities.registerEntities();
		
		PROXY.registerEntityRenderers();
	}
	
	@Mod.EventBusSubscriber
	public static class RegistrationEvents {
		@SubscribeEvent
		@SuppressWarnings("unused")
		public static void blocks(RegistryEvent.Register<Block> e) {
			HaloBlocks.registerBlocks(e.getRegistry());
		}
		
		@SubscribeEvent
		@SuppressWarnings("unused")
		public static void items(RegistryEvent.Register<Item> e) {
			HaloBlocks.registerItemBlocks(e.getRegistry());
			HaloItems.registerItems(e.getRegistry());
		}
		
		@SubscribeEvent
		@SuppressWarnings("unused")
		public static void itemModels(ModelRegistryEvent e) {
			HaloBlocks.registerItemBlockModels();
			HaloItems.registerItemModels();
		}
	}
}
