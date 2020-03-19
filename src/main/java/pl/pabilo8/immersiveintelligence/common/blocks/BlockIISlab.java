/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package pl.pabilo8.immersiveintelligence.common.blocks;

import blusunrize.immersiveengineering.common.blocks.ItemBlockIESlabs;
import blusunrize.immersiveengineering.common.blocks.TileEntityIESlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockIISlab<E extends Enum<E> & BlockIIBase.IBlockEnum> extends BlockIITileProvider<E>
{
	public static final PropertyInteger prop_SlabType = PropertyInteger.create("slabtype", 0, 2);

	public BlockIISlab(String name, Material material, PropertyEnum<E> property)
	{
		super(name, material, property, ItemBlockIESlabs.class, prop_SlabType);
		this.setAllNotNormalBlock();
		this.useNeighborBrightness = true;
	}

	/**
	 * Get the actual Block state of this Block at the given position. This applies properties not visible in the metadata,
	 * such as fence connections.
	 */
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		state = super.getActualState(state, world, pos);
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TileEntityIESlab)
			return state.withProperty(prop_SlabType, ((TileEntityIESlab)tile).slabType);
		return state;
	}
	//	@Override
	//	public IBlockState getStateFromMeta(int meta)
	//	{
	//		return super.getStateFromMeta(meta).withProperty(prop_SlabType, 0);
	//	}

	@Override
	public TileEntity createBasicTE(World worldIn, E meta)
	{
		return new TileEntityIESlab();
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
	}

	/**
	 * Spawns the block's drops in the world. By the time this is called the Block has possibly been set to air via
	 * Block.removedByPlayer
	 */
	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity tile, ItemStack stack)
	{
		if(tile instanceof TileEntityIESlab&&!player.capabilities.isCreativeMode)
		{
			spawnAsEntity(world, pos, new ItemStack(this, ((TileEntityIESlab)tile).slabType==2?2: 1, this.getMetaFromState(state)));
			return;
		}
		super.harvestBlock(world, player, pos, state, tile, stack);
	}

	@Override
	public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileEntityIESlab)
		{
			int type = ((TileEntityIESlab)te).slabType;
			if(type==0)
				return side==EnumFacing.DOWN;
			else if(type==1)
				return side==EnumFacing.UP;
		}
		return true;
	}

	/**
	 * Get the geometry of the queried face at the given position and state. This is used to decide whether things like
	 * buttons are allowed to be placed on the face, or how glass panes connect to the face, among other things.
	 * <p>
	 * Common values are {@code SOLID}, which is the default, and {@code UNDEFINED}, which represents something that does
	 * not fit the other descriptions and will generally cause other things not to connect to the face.
	 *
	 * @return an approximation of the form of the given face
	 * @deprecated call via {@link IBlockState#getBlockFaceShape(IBlockAccess, BlockPos, EnumFacing)} whenever possible.
	 * Implementing/overriding is fine.
	 */
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing side)
	{
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileEntityIESlab)
		{
			int type = ((TileEntityIESlab)te).slabType;
			if(type==2)
				return BlockFaceShape.SOLID;
			else if((type==0&&side==EnumFacing.DOWN)||(type==1&&side==EnumFacing.UP))
				return BlockFaceShape.SOLID;
			else
				return BlockFaceShape.UNDEFINED;
		}
		return BlockFaceShape.SOLID;
	}

	/**
	 * @deprecated call via {@link IBlockState#getBoundingBox(IBlockAccess, BlockPos)} whenever possible.
	 * Implementing/overriding is fine.
	 */
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileEntityIESlab)
		{
			int type = ((TileEntityIESlab)te).slabType;
			if(type==0)
				return new AxisAlignedBB(0, 0, 0, 1, .5f, 1);
			else if(type==1)
				return new AxisAlignedBB(0, .5f, 0, 1, 1, 1);
			else
				return FULL_BLOCK_AABB;
		}
		else
			return new AxisAlignedBB(0, 0, 0, 1, .5f, 1);
	}
}