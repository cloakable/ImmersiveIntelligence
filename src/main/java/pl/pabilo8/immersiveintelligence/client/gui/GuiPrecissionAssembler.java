package pl.pabilo8.immersiveintelligence.client.gui;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first.TileEntityPrecissionAssembler;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerPrecissionAssembler;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.MessageBooleanAnimatedPartsSync;

import java.util.ArrayList;

/**
 * @author Pabilo8
 * @since 10-07-2019
 */
public class GuiPrecissionAssembler extends GuiIEContainerBase
{
	public static final String texture_precission_assembler = ImmersiveIntelligence.MODID+":textures/gui/precission_assembler.png";
	TileEntityPrecissionAssembler tile;
	boolean first_opened;

	public GuiPrecissionAssembler(InventoryPlayer inventoryPlayer, TileEntityPrecissionAssembler tile)
	{
		super(new ContainerPrecissionAssembler(inventoryPlayer, tile));
		this.ySize = 168;
		this.tile = tile;

		first_opened = Math.random() < 0.5d;

		if(first_opened)
			IIPacketHandler.INSTANCE.sendToServer(new MessageBooleanAnimatedPartsSync(true, 0, tile.getPos()));
		else
			IIPacketHandler.INSTANCE.sendToServer(new MessageBooleanAnimatedPartsSync(true, 1, tile.getPos()));

	}

	@Override
	public void onGuiClosed()
	{
		if(first_opened)
			IIPacketHandler.INSTANCE.sendToServer(new MessageBooleanAnimatedPartsSync(false, 0, tile.getPos()));
		else
			IIPacketHandler.INSTANCE.sendToServer(new MessageBooleanAnimatedPartsSync(false, 1, tile.getPos()));
		super.onGuiClosed();
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		this.fontRenderer.drawString(I18n.format("tile."+ImmersiveIntelligence.MODID+".metal_multiblock.precission_assembler.name"), 8, 6, 0x0a0a0a);
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mx, int my)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		ClientUtils.bindTexture(texture_precission_assembler);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		int stored = (int)(47*(tile.getEnergyStored(null)/(float)tile.getMaxEnergyStored(null)));
		ClientUtils.drawGradientRect(guiLeft+159, guiTop+22+(47-stored), guiLeft+166, guiTop+70, 0xffb51500, 0xff600b00);

		if(tile.active&&tile.processTimeMax!=0)
		{
			float progress = Math.min(1f, (float)tile.processTime/(float)tile.processTimeMax);
			this.drawTexturedModalRect(guiLeft+49, guiTop+40, 0, 168, Math.round(85*progress), 17);
		}
	}

	@Override
	public void drawScreen(int mx, int my, float partial)
	{
		super.drawScreen(mx, my, partial);
		this.renderHoveredToolTip(mx, my);

		ArrayList<String> tooltip = new ArrayList();

		if(mx > guiLeft+161&&mx < guiLeft+168&&my > guiTop+24&&my < guiTop+71)
			tooltip.add(tile.getEnergyStored(null)+"/"+tile.getMaxEnergyStored(null)+" IF");

		if(!tooltip.isEmpty())
		{
			ClientUtils.drawHoveringText(tooltip, mx, my, fontRenderer, guiLeft+xSize, -1);
			RenderHelper.enableGUIStandardItemLighting();
		}
	}
}
