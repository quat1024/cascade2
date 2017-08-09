package quaternary.cascade2.net;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.relauncher.Side;

public abstract class CascadeMessage implements IMessage {
	public abstract Side getSide();
	public abstract IMessageHandler getHandler();
	
	public abstract class Handler implements IMessageHandler {}
}
