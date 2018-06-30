package timaxa007.classification;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import timaxa007.classification.network.SyncClassificationMessage;

public class EventsForge {

	@SubscribeEvent
	public void addEntityConstructing(EntityEvent.EntityConstructing event) {
		if (event.entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)event.entity;
			if (ClassificationPlayer.get(player) == null) ClassificationPlayer.reg(player);
		}
	}

	@SubscribeEvent
	public void cloneClassificationPlayer(PlayerEvent.Clone event) {
		ClassificationPlayer originalClassificationPlayer = ClassificationPlayer.get((EntityPlayer)event.original);
		if (originalClassificationPlayer == null) return;
		ClassificationPlayer newClassificationPlayer = ClassificationPlayer.get((EntityPlayer)event.entityPlayer);
		if (newClassificationPlayer == null) return;
		newClassificationPlayer.setClassification(originalClassificationPlayer.getClassification());
	}

	@SubscribeEvent
	public void syncClassificationPlayer(EntityJoinWorldEvent event) {
		if (event.entity instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP)event.entity;
			ClassificationPlayer classPlayer = ClassificationPlayer.get(player);
			if (classPlayer == null) return;
			SyncClassificationMessage message = new SyncClassificationMessage();
			message.classification = classPlayer.getClassification().getID();
			ClassificationMod.network.sendTo(message, player);
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void drawText(RenderGameOverlayEvent.Post event) {
		switch(event.type) {
		case TEXT:
			ClassificationPlayer classPlayer = ClassificationPlayer.get(Minecraft.getMinecraft().thePlayer);
			if (classPlayer == null) return;
			Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(
					StatCollector.translateToLocalFormatted("classification.info", new Object[] {StatCollector.translateToLocal("classification." + classPlayer.getClassification().getTag() + ".name")}),
					3, 2 + (10 * 3), 0xFFFFFF);
			break;
		default:return;
		}
	}

}
