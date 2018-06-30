package timaxa007.classification.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import timaxa007.classification.ClassificationMod;
import timaxa007.classification.ClassificationPlayer;
import timaxa007.classification.network.SyncClassificationMessage;

public class SelectClassificationGui extends GuiScreen {

	private static final ResourceLocation texture = new ResourceLocation("classification", "textures/gui/classification_back.png");
	final int sizeX = 116;
	int sizeY = 0;
	int posX = 0;
	int posY = 0;

	@Override
	public void initGui() {
		posX = (width - sizeX) / 2;
		sizeY = (ClassificationPlayer.Classification.values().length * 22) + 16;
		posY = (height - sizeY) / 2;
		int pY = 8;
		for (int i = 1; i < ClassificationPlayer.Classification.values().length; ++i) {
			ClassificationPlayer.Classification classification = ClassificationPlayer.Classification.values()[i];
			buttonList.add(new GuiButton(classification.getID(), posX + 8, posY + ((i - 1) * 22) + 9, 100, 20, StatCollector.translateToLocal("classification." + classification.getTag() + ".name")));
		}
		buttonList.add(new GuiButton(0, posX + 8, posY + ((ClassificationPlayer.Classification.values().length - 1) * 22) + 9, 100, 20, StatCollector.translateToLocal("gui.cancel")));
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (!button.enabled) return;
		if (button.id != 0) {
			SyncClassificationMessage message = new SyncClassificationMessage();
			message.classification = (byte)button.id;
			ClassificationMod.network.sendToServer(message);
		}
		mc.displayGuiScreen(null);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float parTick) {
		mc.getTextureManager().bindTexture(texture);
		drawTexturedModalRect(posX, posY, 0, 0, sizeX, 8);//top
		for (int i = 0; i < ClassificationPlayer.Classification.values().length; ++i) {
			drawTexturedModalRect(posX, posY + (i * 22) + 8, 0, 8, sizeX, 22);
		}
		drawTexturedModalRect(posX, posY + (ClassificationPlayer.Classification.values().length * 22) + 8, 0, 30, sizeX, 8);//botton

		super.drawScreen(mouseX, mouseY, parTick);
	}

}
