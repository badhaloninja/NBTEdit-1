package malte0811.nbtedit.network;

import io.netty.buffer.ByteBuf;
import malte0811.nbtedit.NBTEdit;
import malte0811.nbtedit.nbt.EditPosKey;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageNBTSync implements IMessage {
	EditPosKey pos;
	NBTTagCompound value;
	public MessageNBTSync(EditPosKey k, NBTTagCompound val) {
		pos = k;
		value = val;
	}

	public MessageNBTSync() {}

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

	public static class ClientHandler implements IMessageHandler<MessageNBTSync, IMessage>
	{
		@Override
		public IMessage onMessage(MessageNBTSync msg, MessageContext ctx)
		{
			synchronized (NBTEdit.proxy) {
				NBTEdit.proxy.cache(msg.pos, msg.value);
				NBTEdit.proxy.notifyAll();
			}
			return null;
		}
	}
}
