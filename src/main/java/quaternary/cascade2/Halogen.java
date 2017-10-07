package quaternary.cascade2;

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
import quaternary.cascade2.block.ModBlocks;
import quaternary.cascade2.cap.HaloCaps;
import quaternary.cascade2.item.ModItems;
import quaternary.cascade2.misc.CascadeCreativeTab;
import quaternary.cascade2.proxy.CommonProxy;

@Mod(modid = Halogen.MODID, name = Halogen.NAME, version = Halogen.VERSION)
public class Halogen {
	public static final String MODID = "halogen";
	public static final String NAME = "Halogen";
	public static final String VERSION = "0";
	
	@Mod.Instance
	public static Halogen INSTANCE = new Halogen();
	
	@SidedProxy(clientSide = "quaternary.cascade2.proxy.ClientProxy",
					serverSide = "quaternary.cascade2.proxy.CommonProxy")
	public static CommonProxy PROXY;
	
	public static final CascadeCreativeTab CREATIVE_TAB = new CascadeCreativeTab();
	public static final Logger LOGGER = LogManager.getLogger(MODID);
	
	@Mod.EventHandler
	@SuppressWarnings("unused")
	public void preinit(FMLPreInitializationEvent e) {
		//PROXY.registerTESRs();
		
		HaloCaps.registerCaps();
	}
	
	@Mod.EventBusSubscriber
	public static class RegistrationEvents {
		@SubscribeEvent
		@SuppressWarnings("unused")
		public static void blocks(RegistryEvent.Register<Block> e) {
			ModBlocks.registerBlocks(e.getRegistry());
		}
		
		@SubscribeEvent
		@SuppressWarnings("unused")
		public static void items(RegistryEvent.Register<Item> e) {
			ModBlocks.registerItemBlocks(e.getRegistry());
			ModItems.registerItems(e.getRegistry());
		}
		
		@SubscribeEvent
		@SuppressWarnings("unused")
		public static void itemModels(ModelRegistryEvent e) {
			ModBlocks.registerItemBlockModels();
			ModItems.registerItemModels();
		}
	}
}