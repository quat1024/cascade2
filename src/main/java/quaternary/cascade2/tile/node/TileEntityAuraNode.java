package quaternary.cascade2.tile.node;
// Comment with space after the slashes to appease Nerxit.

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import quaternary.cascade2.Cascade;
import quaternary.cascade2.cap.CascadeCaps;
import quaternary.cascade2.cap.aura.AuraHolderBase;
import quaternary.cascade2.cap.aura.IAuraHolder;
import quaternary.cascade2.cap.aura.connection.AuraConnectableBase;
import quaternary.cascade2.cap.aura.connection.ConnectionData;
import quaternary.cascade2.cap.aura.connection.IAuraConnectable;
import quaternary.cascade2.tile.CascadeTileEntity;
import quaternary.cascade2.util.CascadeUtils;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TileEntityAuraNode extends CascadeTileEntity implements ITickable {
	
	private static final int CONNECTION_RANGE = 16;
	private static final AxisAlignedBB ITEM_DETECTION_AABB = new AxisAlignedBB(0, 0, 0, 1, 1, 1);
	
	private final IAuraHolder auraHolderCap = new AuraHolderBase(1000);
	private final IAuraConnectable auraConnectCap = new AuraConnectableBase();
	
	private byte absorptionCooldown = 0;
	
	//This is a thing I could put in a cap
	//private ConcurrentHashMap<EnumFacing, ConnectionData> connections = new ConcurrentHashMap<>();
	
	public void update() {
		//benis
	}
	
	@Override
	public boolean hasCapability(Capability<?> hat, EnumFacing side) {
		if(hat == CascadeCaps.AURA_HOLDER || hat == CascadeCaps.AURA_CONNECTABLE) {
			return true;
		} else return super.hasCapability(hat, side);
	}
	
	@Override
	public <T> T getCapability(@Nonnull Capability<T> hat, EnumFacing side) {
		if(hat == CascadeCaps.AURA_HOLDER) {
			return CascadeCaps.AURA_HOLDER.cast(auraHolderCap);
		} else if(hat == CascadeCaps.AURA_CONNECTABLE) {
			return CascadeCaps.AURA_CONNECTABLE.cast(auraConnectCap);
		} else return super.getCapability(hat, side);
	}
	
	//todo: these just skip over and call the capability
	//how about, instead of calling these and tunneling right to the cap,
	//just call the capability directly :thinking:
	private void scanForConnections() {
		auraConnectCap.rescanForConnections(world, pos);
	}
	
	private void resetAllConnections() {
		auraConnectCap.eraseConnections();
	}
	
	//todo: dirty flag this maybe?
	//because it will only ever update when the connection map does, if then
	//todo: does this belong in the capability? probably right?
	public ConcurrentHashMap<EnumFacing, ConnectionData> getActiveConnections() {
		// This should work but doesn't...
		/*
		return auraConnectCap.getConnectionMap()
						.entrySet().stream()
						.filter(entry -> entry.getValue().unblocked)
						.collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
		*/
		
		ConcurrentHashMap<EnumFacing, ConnectionData> blah = new ConcurrentHashMap<>();
		for(Map.Entry<EnumFacing, ConnectionData> pair : auraConnectCap.getConnectionMap().entrySet()) {
			if(pair.getValue().unblocked) {
				blah.put(pair.getKey(), pair.getValue());
			}
		}
		return blah;
	}
	
	//Events called from blockauranode
	public void onActivated(World w, EntityPlayer player) {
		//CascadeUtils.sendChatMessage(player, world.isRemote ? "Hi i'm the client" : "I'm the server");
		
		auraConnectCap.lalalaDebugPrintOwoWhatsThis(w, player);
	}
	
	public void onPlaceBlock() {
		scanForConnections();
	}
	
	public void onBreakBlock() {
		world.removeTileEntity(pos); //This is stupid and dangerous
		auraConnectCap.onRemoved(world, pos);
	}
	
	//NBT save load junk
	private static final String AURA_KEY = "Aura";
	private static final String ABSORPTION_COOLDOWN_KEY = "AuraCooldown";
	private static final String CONNECTIONS_LIST_KEY = "Connections";
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setTag(AURA_KEY, auraHolderCap.writeNBT());
		nbt.setByte(ABSORPTION_COOLDOWN_KEY, absorptionCooldown);
		nbt.setTag(CONNECTIONS_LIST_KEY, auraConnectCap.writeNBT());
		
		return super.writeToNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		auraHolderCap.readNBT(nbt.getTagList(AURA_KEY, 10)); //The 10 is some magic number idk.
		absorptionCooldown = nbt.getByte(ABSORPTION_COOLDOWN_KEY);
		Cascade.LOGGER.info(nbt.getTagList(CONNECTIONS_LIST_KEY,10));
		auraConnectCap.readNBT(nbt.getTagList(CONNECTIONS_LIST_KEY, 10));
		
		super.readFromNBT(nbt);
	}
}
