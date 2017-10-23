package quaternary.halogen.entity;

import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import quaternary.halogen.Halogen;
import quaternary.halogen.recipe.HaloRiftRecipes;
import quaternary.halogen.recipe.RiftRecipe;

import java.util.Optional;

public class EntityRift extends Entity {
	//TODO: Don't track the radius, because it's susceptible to cumulative floating point error.
	//Would be nice to store: the last time the radius was changed, the radius at that time, and the
	//ticks since that last change; and determine the radius from there.
	private static final DataParameter<Float> RADIUS = EntityDataManager.createKey(EntityRift.class, DataSerializers.FLOAT);
	private static final DataParameter<Integer> COOLDOWN = EntityDataManager.createKey(EntityRift.class, DataSerializers.VARINT);
	
	public EntityRift(World w) {
		super(w);
		noClip = true;
		
		setRad(1f);
	}
	
	@Override
	protected void entityInit() {
		dataManager.register(RADIUS, 1f);
		dataManager.register(COOLDOWN, 0);
	}
	
	@Override
	public void onUpdate() {
		float r = dataManager.get(RADIUS);
		
		int cooldown = dataManager.get(COOLDOWN);
		
		if(cooldown > 0) {
			cooldown--;
		} else {
			AxisAlignedBB itemDetectionAABB = new AxisAlignedBB(posX - r, posY - 0.25, posZ - r, posX + r, posY + 0.25, posZ + r);
			for(EntityItem item : world.getEntitiesWithinAABB(EntityItem.class, itemDetectionAABB)) {
				if(item == null || item.isDead || item.getItem/*Stack*/().isEmpty()) continue;
				
				ItemStack input = item.getItem/*Stack*/();
				Optional<ItemStack> outputMaybe = HaloRiftRecipes.getOutput(input);
				
				if(outputMaybe.isPresent()) {
					ItemStack output = outputMaybe.get();
					if(!world.isRemote) {
						EntityItem outputEntity = new EntityItem(world, item.posX, item.posY, item.posZ, output);
						outputEntity.setPickupDelay(15);
						outputEntity.addVelocity(0,0.3,0);
						world.spawnEntity(outputEntity);
					}
					
					//TODO: handle recipes with more than one input (like 4 inputs > 1 output)
					input.shrink(1);
					
					cooldown = 5;
					r -= 0.1;
					
					break;
				}
			}
		}
		
		dataManager.set(COOLDOWN, cooldown);
		
		if(r <= 0) {
			setDead();
			return;
		}
		
		setRad(r - 0.005f);
	}
	
	void setRad(float radius) {
		float entityHeight = radius > 1f? 0.5f : radius/2f;
		
		setSize(radius*2, entityHeight);
		
		dataManager.set(RADIUS, radius);
	}
	
	@Override
	public void setPosition(double x, double y, double z) {
		super.setPosition(Math.floor(x) + 0.5, Math.floor(y), Math.floor(z) + 0.5);
	}
	
	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		
	}
	
	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		
	}
	
	@GameRegistry.ObjectHolder("halogen:rift")
	public static final Item rift = null;
	
	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(rift);
	}
	
	@Override
	public EnumPushReaction getPushReaction() {
		return EnumPushReaction.IGNORE;
	}
}
