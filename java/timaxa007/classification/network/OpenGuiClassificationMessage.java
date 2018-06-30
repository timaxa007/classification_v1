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
import timaxa007.classification.client.gui.SelectClassificationGui;

public class OpenGuiClassificationMessage implements IMessage {

	public OpenGuiClassificationMessage() {}

	@Override
	public void toBytes(ByteBuf buf) {}

	@Override
	public void fromBytes(ByteBuf buf) {}

	public static class Handler implements IMessageHandler<OpenGuiClassificationMessage, IMessage> {

		@Override
		public IMessage onMessage(OpenGuiClassificationMessage packet, MessageContext message) {
			if (message.side.isClient())
				act(packet);
			else
				act(message.getServerHandler().playerEntity, packet);
			return null;
		}

		@SideOnly(Side.CLIENT)
		private void act(OpenGuiClassificationMessage packet) {
			Minecraft mc = Minecraft.getMinecraft();
			if (ClassificationPlayer.get(mc.thePlayer) == null) return;
			mc.displayGuiScreen(new SelectClassificationGui());
		}

		private void act(EntityPlayerMP player, OpenGuiClassificationMessage packet) {
			if (ClassificationPlayer.get(player) == null) return;
			ClassificationMod.network.sendTo(new OpenGuiClassificationMessage(), player);
		}

	}

}
