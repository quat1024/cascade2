package quaternary.halogen;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import quaternary.halogen.block.HaloBlock;
import quaternary.halogen.cap.HaloCaps;
import quaternary.halogen.entity.HaloEntities;
import quaternary.halogen.item.HaloItem;
import quaternary.halogen.misc.HaloCreativeTab;
import quaternary.halogen.proxy.CommonProxy;
import quaternary.halogen.recipe.HaloRiftRecipes;
import quaternary.halogen.tile.HaloTiles;

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
		HaloCaps.registerCaps();
		HaloEntities.registerEntities();
		
		PROXY.registerEntityRenderers();
		//PROXY.registerTESRs();
	}	
	
	@Mod.EventHandler
	@SuppressWarnings("unused")
	public void init(FMLInitializationEvent e) {
		HaloRiftRecipes.registerRecipes();
	}
	
	@Mod.EventBusSubscriber
	public static class RegistrationEvents {
		@SubscribeEvent
		@SuppressWarnings("unused")
		public static void blocks(RegistryEvent.Register<Block> e) {
			IForgeRegistry<Block> reg = e.getRegistry();
			
			reg.registerAll(HaloStuff.BLOCKS);
			
			//dun mind me just gonna sneak this in right here
			HaloTiles.registerTiles();
		}
		
		@SubscribeEvent
		@SuppressWarnings("unused")
		public static void items(RegistryEvent.Register<Item> e) {
			IForgeRegistry<Item> reg = e.getRegistry();
			
			for(HaloBlock b : HaloStuff.BLOCKS) {
				reg.register(b.getItemBlock());
			}
			
			reg.registerAll(HaloStuff.ITEMS);
		}
		
		@SubscribeEvent
		@SuppressWarnings("unused")
		public static void itemModels(ModelRegistryEvent e) {			
			for(HaloBlock b : HaloStuff.BLOCKS) {
				b.registerItemBlockModel();
			}
			
			for(HaloItem i : HaloStuff.ITEMS) {
				ModelLoader.setCustomModelResourceLocation(i, 0,
				new ModelResourceLocation(i.getRegistryName(), "inventory"));
			}
		}
	}
}
