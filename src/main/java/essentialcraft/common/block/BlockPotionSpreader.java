package essentialcraft.common.block;

import DummyCore.Client.IModelRegisterer;
import essentialcraft.common.mod.EssentialCraftCore;
import essentialcraft.common.tile.TilePotionSpreader;
import essentialcraft.utils.cfg.Config;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockPotionSpreader extends BlockContainer implements IModelRegisterer {

	public BlockPotionSpreader() {
		super(Material.ROCK, MapColor.PURPLE);
	}

	@Override
	public void breakBlock(World par1World, BlockPos par2Pos, IBlockState par3State) {
		IInventory inv = (IInventory)par1World.getTileEntity(par2Pos);
		InventoryHelper.dropInventoryItems(par1World, par2Pos, inv);
		super.breakBlock(par1World, par2Pos, par3State);
	}

	@Override
	public boolean isOpaqueCube(IBlockState s)
	{
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState s)
	{
		return false;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState s)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TilePotionSpreader();
	}

	@Override
	public boolean onBlockActivated(World par1World, BlockPos par2, IBlockState par3, EntityPlayer par4EntityPlayer, EnumHand par5, EnumFacing par7, float par8, float par9, float par10) {
		if(par4EntityPlayer.isSneaking()) {
			return false;
		}
		if(!par1World.isRemote) {
			par4EntityPlayer.openGui(EssentialCraftCore.core, Config.guiID[0], par1World, par2.getX(), par2.getY(), par2.getZ());
			return true;
		}
		return true;
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("essentialcraft:potionspreader", "inventory"));
	}
}
