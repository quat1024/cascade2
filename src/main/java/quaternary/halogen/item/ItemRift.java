package quaternary.halogen.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import quaternary.halogen.entity.EntityThrownRift;

public class ItemRift extends HaloItem {
	public ItemRift() {
		super("rift");
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		
		if(!player.capabilities.isCreativeMode) stack.shrink(1);
		
		if(!world.isRemote) {
			EntityThrownRift e = new EntityThrownRift(world, player);
			//             that constructor ^ takes care of setting the position from the player
			//             not the orientation though, so let's fix that
			e.shoot(player, player.rotationPitch, player.rotationYaw, 0f, 0.7f, 1f);
			world.spawnEntity(e);
		}
		
		return new ActionResult<>(EnumActionResult.SUCCESS, stack);
	}
}
