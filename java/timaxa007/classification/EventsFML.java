package timaxa007.classification;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import timaxa007.classification.network.OpenGuiClassificationMessage;

public class EventsFML {

	@SubscribeEvent
	public void loging(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.player instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP)event.player;
			ClassificationPlayer classPlayer = ClassificationPlayer.get(player);
			if (classPlayer == null) return;
			if (classPlayer.getClassification() == ClassificationPlayer.Classification.NONE)
				ClassificationMod.network.sendTo(new OpenGuiClassificationMessage(), (EntityPlayerMP)player);
		}
	}

}
