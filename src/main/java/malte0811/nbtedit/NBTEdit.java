package malte0811.nbtedit;

import malte0811.nbtedit.client.NBTClipboard;
import malte0811.nbtedit.command.CommandNbtEdit;
import malte0811.nbtedit.nbt.CommonProxy;
import malte0811.nbtedit.network.MessageNBTSync;
import malte0811.nbtedit.network.MessageOpenWindow;
import malte0811.nbtedit.network.MessagePushNBT;
import malte0811.nbtedit.network.MessageRequestNBT;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = NBTEdit.MODID, version = NBTEdit.VERSION)
public class NBTEdit
{
    public static final String MODID = "nbtedit";
    public static final String VERSION = "$version";
	public static final SimpleNetworkWrapper packetHandler = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
	@SidedProxy(clientSide="malte0811.nbtedit.nbt.ClientProxy", serverSide="malte0811.nbtedit.nbt.CommonProxy")
	public static CommonProxy proxy;
	public static CommonProxy commonProxyInstance = new CommonProxy();
	public static CommandNbtEdit editNbt;

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	int id = 0;
    	packetHandler.registerMessage(MessageNBTSync.ClientHandler.class, MessageNBTSync.class, id++, Side.CLIENT);
    	packetHandler.registerMessage(MessageOpenWindow.ClientHandler.class, MessageOpenWindow.class, id++, Side.CLIENT);
    	packetHandler.registerMessage(MessagePushNBT.ServerHandler.class, MessagePushNBT.class, id++, Side.SERVER);
    	packetHandler.registerMessage(MessageRequestNBT.ServerHandler.class, MessageRequestNBT.class, id++, Side.SERVER);
    	if (event.getSide()==Side.CLIENT) {
    		NBTClipboard.readFromDisc();
    		Compat.registerHandlers();
    	}
    }
    @EventHandler
    public void serverStart(FMLServerStartingEvent ev)
    {
    	editNbt = new CommandNbtEdit();
    	ev.registerServerCommand(editNbt);
    }
}
