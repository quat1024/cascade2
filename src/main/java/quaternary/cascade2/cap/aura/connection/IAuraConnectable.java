package quaternary.cascade2.cap.aura.connection;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.Logger;
import quaternary.cascade2.cap.ISaveLoadCapability;

import java.util.concurrent.ConcurrentHashMap;

/** Things that can send or receive aura.
 * Think like aura nodes, aura pumps, blah.
 * See also: IAuraProvider, IAuraAcceptor */
//Todo: Don't hardcode blockposes? Is it possible to extend this to entities?
public interface IAuraConnectable extends ISaveLoadCapability<IAuraConnectable> {
	//Map manipulation
	ConcurrentHashMap<EnumFacing, ConnectionData> getConnectionMap();
	void replaceConnectionMap(ConcurrentHashMap<EnumFacing, ConnectionData> newMap);
	void eraseConnections();
	
	//more "local" connection management methods
	void rescanForConnections(World world, BlockPos pos);
	void setConnection(EnumFacing key, ConnectionData value);
	
	//things pertaining to adding and removing
	void onRemoved(World world, BlockPos pos);
	
	void lalalaDebugPrintOwoWhatsThis(World w, EntityPlayer p); //TODO: Delet this
}
