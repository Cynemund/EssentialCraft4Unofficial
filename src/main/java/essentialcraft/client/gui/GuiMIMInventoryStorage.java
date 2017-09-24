package essentialcraft.client.gui;

import DummyCore.Utils.DrawUtils;
import essentialcraft.common.inventory.ContainerMIMInventoryStorage;
import essentialcraft.common.tile.TileMIMInventoryStorage;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiMIMInventoryStorage extends GuiContainer{

	TileMIMInventoryStorage tile;

	public GuiMIMInventoryStorage(InventoryPlayer inventoryPlayer, TileMIMInventoryStorage t)
	{
		super(new ContainerMIMInventoryStorage(inventoryPlayer, t));
		tile = t;

		xSize = 176;
		ySize = 222;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks,int mX, int mY) {
		int k = (this.width - this.xSize)/2;
		int l = (this.height - this.ySize)/2;

		DrawUtils.bindTexture("essentialcraft", "textures/gui/magical_chest.png");

		this.drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
	}
}
