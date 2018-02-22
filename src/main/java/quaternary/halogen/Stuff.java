package quaternary.halogen;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import quaternary.halogen.aura.type.AuraTypes;
import quaternary.halogen.block.BlockBase;
import quaternary.halogen.block.node.BlockNode;
import quaternary.halogen.block.plain.BlockRiftStone;
import quaternary.halogen.entity.EntityRift;
import quaternary.halogen.item.HaloItem;
import quaternary.halogen.item.ItemAuraCrystal;

import java.util.ArrayList;
import java.util.List;

public class Stuff {
	public static List<Block> BLOCKS = new ArrayList<>();
	public static List<Item> ITEMS = new ArrayList<>();
	public static List<EntityEntry> ENTITIES = new ArrayList<>();
	
	static {
		//Blocks!
		BLOCKS.add(new BlockNode("node_white"));
		BLOCKS.add(new BlockRiftStone("normal"));
		BLOCKS.add(new BlockRiftStone("polished"));
		BLOCKS.add(new BlockRiftStone("chiseled"));
		
		//Items!
		for(Block b : BLOCKS) {
			if(b instanceof BlockBase) {
				BlockBase bb = (BlockBase) b;
				
				ITEMS.add(bb.getItemForm());
			}
		}
		
		ITEMS.add(new ItemAuraCrystal(AuraTypes.NORMAL));
		ITEMS.add(new HaloItem("rift_dust"));
		
		//Entities!
		ENTITIES.add(ent(EntityRift.class, "rift", 96, 5, false));
	}
	
	private static int i = 0;
	private static EntityEntry ent(Class c, String id, int range, int freq, boolean velocity) {
		return EntityEntryBuilder.create().entity(c).id(new ResourceLocation(Halogen.MODID, id), i++).name(Halogen.MODID + id).tracker(range, freq, velocity).build(); 
	}
}
