package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.gui.ContainerCrate;
import net.minecraft.entity.player.InventoryPlayer;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.TileEntityMetalCrate;

/**
 * @author Pabilo8
 * @since 2019-05-17
 */
public class ContainerMetalCrate extends ContainerCrate
{
	public ContainerMetalCrate(InventoryPlayer inventoryPlayer, TileEntityMetalCrate tile)
	{
		super(inventoryPlayer, tile);
	}
}
