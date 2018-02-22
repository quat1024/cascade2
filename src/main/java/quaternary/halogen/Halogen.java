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
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import quaternary.halogen.block.BlockBase;
import quaternary.halogen.cap.HaloCaps;
import quaternary.halogen.misc.HaloCreativeTab;
import quaternary.halogen.proxy.CommonProxy;
import quaternary.halogen.recipe.HaloRiftRecipes;

@Mod(modid = Halogen.MODID, name = Halogen.NAME, version = Halogen.VERSION)
public class Halogen {
	public static final String MODID = "halogen";
	public static final String NAME = "Halogen";
	//this is replaced with Gradle magic
	public static final String VERSION = "gradle:modVersion";
	
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
		
		PROXY.registerEntityRenderers();
	}
	
	@Mod.EventHandler
	@SuppressWarnings("unused")
	public void init(FMLInitializationEvent e) {
		HaloRiftRecipes.registerRecipes();
	}
	
	@Mod.EventBusSubscriber(modid = Halogen.MODID)
	public static class RegistrationEvents {
		@SubscribeEvent
		@SuppressWarnings("unused")
		public static void blocks(RegistryEvent.Register<Block> e) {
			for(Block b : Stuff.BLOCKS) {
				e.getRegistry().register(b);
				
				if(b instanceof BlockBase) {
					BlockBase bb = (BlockBase) b;
					
					if(bb.hasTileEntity()) {
						GameRegistry.registerTileEntity(bb.getTileEntityClass(), bb.getRegistryName().toString());
					}
				}
			}
		}
		
		@SubscribeEvent
		@SuppressWarnings("unused")
		public static void items(RegistryEvent.Register<Item> e) {
			for(Item i : Stuff.ITEMS) {
				e.getRegistry().register(i);
			}
		}
		
		@SubscribeEvent
		@SuppressWarnings("unused")
		public static void entities(RegistryEvent.Register<EntityEntry> e) {
			for(EntityEntry ent : Stuff.ENTITIES) {
				e.getRegistry().register(ent);
			}
		}
	}
	
	@Mod.EventBusSubscriber(modid = Halogen.MODID, value = Side.CLIENT)
	public static class ClientEvents {
		@SubscribeEvent
		@SuppressWarnings("unused")
		public static void itemModels(ModelRegistryEvent e) {
			for(Item i : Stuff.ITEMS) {
				//What is data value :S
				ModelResourceLocation mrl = new ModelResourceLocation(i.getRegistryName(), "inventory");
				ModelLoader.setCustomModelResourceLocation(i, 0, mrl);
			}
		}
	}
}
