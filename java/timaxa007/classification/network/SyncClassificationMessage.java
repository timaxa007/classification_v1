package timaxa007.classification.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import timaxa007.classification.ClassificationMod;
import timaxa007.classification.ClassificationPlayer;

public class SyncClassificationMessage implements IMessage {

	public byte classification;

	public SyncClassificationMessage() {}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(classification);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		classification = buf.readByte();
	}

	public static class Handler implements IMessageHandler<SyncClassificationMessage, IMessage> {

		@Override
		public IMessage onMessage(SyncClassificationMessage packet, MessageContext message) {
			if (message.side.isClient())
				act(packet);
			else
				act(message.getServerHandler().playerEntity, packet);
			return null;
		}

		@SideOnly(Side.CLIENT)
		private void act(SyncClassificationMessage packet) {
			Minecraft mc = Minecraft.getMinecraft();
			ClassificationPlayer classPlayer = ClassificationPlayer.get(mc.thePlayer);
			if (classPlayer == null) return;
			classPlayer.setClassification(ClassificationPlayer.Classification.getClassification(packet.classification));
		}

		private void act(EntityPlayerMP player, SyncClassificationMessage packet) {
			ClassificationPlayer classPlayer = ClassificationPlayer.get(player);
			if (classPlayer == null) return;
			classPlayer.setClassification(ClassificationPlayer.Classification.getClassification(packet.classification));

			SyncClassificationMessage message = new SyncClassificationMessage();
			message.classification = packet.classification;
			ClassificationMod.network.sendTo(message, player);
		}

	}

}
