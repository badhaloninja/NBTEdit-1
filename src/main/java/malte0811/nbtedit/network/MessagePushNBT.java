package malte0811.nbtedit.network;

import org.apache.logging.log4j.Level;

import io.netty.buffer.ByteBuf;
import malte0811.nbtedit.NBTEdit;
import malte0811.nbtedit.nbt.EditPosKey;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessagePushNBT implements IMessage {
	EditPosKey pos;
	NBTTagCompound value;
	public MessagePushNBT(EditPosKey k, NBTTagCompound val) {
		pos = k;
		value = val;
	}
	
	public MessagePushNBT() {}

	@Override
	public void fromBytes(ByteBuf buf) {
		pos = EditPosKey.fromBytes(buf);
		value = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		pos.toBytes(buf);
		ByteBufUtils.writeTag(buf, value);
	}

	public static class ServerHandler implements IMessageHandler<MessagePushNBT, IMessage>
	{
		@Override
		public IMessage onMessage(MessagePushNBT msg, MessageContext ctx)
		{
			if (NBTEdit.editNbt.canCommandSenderUseCommand(ctx.getServerHandler().playerEntity)) {
				NBTEdit.commonProxyInstance.setNBT(msg.pos, msg.value);
				return null;
			}
			FMLLog.log(NBTEdit.MODID, Level.ERROR, "Player "+ctx.getServerHandler().playerEntity.getDisplayNameString()+" tried to push NBT data to the server but isn't permitted to do so!");
			return null;
		}
	}
}
