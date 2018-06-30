package timaxa007.classification;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.MinecraftForge;
import timaxa007.classification.command.ClassificationGuiCommand;
import timaxa007.classification.command.ClassificationSetCommand;
import timaxa007.classification.network.OpenGuiClassificationMessage;
import timaxa007.classification.network.SyncClassificationMessage;

@Mod(modid = ClassificationMod.MODID, name = ClassificationMod.NAME, version = ClassificationMod.VERSION)
public final class ClassificationMod {

	public static final String
	MODID = "classification1",
	NAME = "Classification Mod v1",
	VERSION = "1";

	@Mod.Instance(MODID) public static ClassificationMod instance;

	public static final SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		network.registerMessage(SyncClassificationMessage.Handler.class, SyncClassificationMessage.class, 0, Side.CLIENT);
		network.registerMessage(SyncClassificationMessage.Handler.class, SyncClassificationMessage.class, 0, Side.SERVER);
		network.registerMessage(OpenGuiClassificationMessage.Handler.class, OpenGuiClassificationMessage.class, 1, Side.CLIENT);
		network.registerMessage(OpenGuiClassificationMessage.Handler.class, OpenGuiClassificationMessage.class, 1, Side.SERVER);
		MinecraftForge.EVENT_BUS.register(new EventsForge());
		FMLCommonHandler.instance().bus().register(new EventsFML());
	}

	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new ClassificationSetCommand());
		event.registerServerCommand(new ClassificationGuiCommand());
	}

}
