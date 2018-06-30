package timaxa007.classification.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.StatCollector;
import timaxa007.classification.ClassificationMod;
import timaxa007.classification.ClassificationPlayer;
import timaxa007.classification.network.OpenGuiClassificationMessage;

public class ClassificationGuiCommand extends CommandBase {

	@Override
	public String getCommandName() {
		return "classification_gui";
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender ics) {
		if (ics instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP)ics;
			ClassificationPlayer classPlayer = ClassificationPlayer.get(player);
			if (classPlayer != null) return true;
		}
		return false;
	}

	/**Return the required permission level for this command.**/
	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public String getCommandUsage(ICommandSender ics) {
		return "commands.classification_gui.usage";
	}

	@Override
	public void processCommand(ICommandSender ics, String[] args) {
		if (ics instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP)ics;
			ClassificationPlayer classPlayer = ClassificationPlayer.get(player);
			if (classPlayer == null) return;
			if (classPlayer.getClassification() == ClassificationPlayer.Classification.NONE)
				ClassificationMod.network.sendTo(new OpenGuiClassificationMessage(), (EntityPlayerMP)player);
			else
				ics.addChatMessage(new ChatComponentTranslation("classification.info", StatCollector.translateToLocal("classification." + classPlayer.getClassification().getTag() + ".name")));
		}
	}

}
