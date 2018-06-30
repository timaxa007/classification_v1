package timaxa007.classification.command;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.StatCollector;
import timaxa007.classification.ClassificationPlayer;

public class ClassificationSetCommand extends CommandBase {

	@Override
	public String getCommandName() {
		return "classification_set";
	}

	/**Return the required permission level for this command.**/
	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getCommandUsage(ICommandSender ics) {
		return "commands.classification_set.usage";
	}

	@Override
	public void processCommand(ICommandSender ics, String[] args) {
		if (args.length != 2) {
			throw new WrongUsageException(getCommandUsage(ics), new Object[0]);
		} else {
			EntityPlayerMP player = getPlayer(ics, args[0]);
			ClassificationPlayer classPlayer = ClassificationPlayer.get(player);
			if (classPlayer == null) return;

			ClassificationPlayer.Classification classification = ClassificationPlayer.Classification.getClassification(args[1]);

			if (classification == null)
				classification = ClassificationPlayer.Classification.getClassification((byte)parseIntBounded(ics, args[1], Byte.MIN_VALUE, Byte.MAX_VALUE));

			classPlayer.setClassification(classification);
			ics.addChatMessage(new ChatComponentTranslation("classification.info", StatCollector.translateToLocal("classification." + classPlayer.getClassification().getTag() + ".name")));
		}
	}

	/**Adds the strings available in this command to the given list of tab completion options.**/
	@Override
	public List addTabCompletionOptions(ICommandSender ics, String[] args) {
		return args.length == 1 ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : 
			args.length == 2 ? getListOfStringsMatchingLastWord(args, ClassificationPlayer.Classification.getClassificationTags()) : null;
	}

	/**Return whether the specified command parameter index is a username parameter.**/
	@Override
	public boolean isUsernameIndex(String[] args, int id) {
		return id == 0;
	}

}
