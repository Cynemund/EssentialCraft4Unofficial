package essentialcraft.common.block;

import DummyCore.Client.IModelRegisterer;
import essentialcraft.common.tile.TileMRUCUECRedstoneController;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockMRUCUECRedstoneController extends BlockContainer implements IModelRegisterer {

	protected BlockMRUCUECRedstoneController(Material p_i45386_1_) {
		super(p_i45386_1_);
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileMRUCUECRedstoneController();
	}

	@Override
	public boolean onBlockActivated(World par1World, BlockPos par2, IBlockState par3, EntityPlayer par4EntityPlayer, EnumHand par5, EnumFacing par7, float par8, float par9, float par10) {
		if(par4EntityPlayer.isSneaking()) {
			TileMRUCUECRedstoneController rc = (TileMRUCUECRedstoneController)par1World.getTileEntity(par2);
			rc.setting += 1;
			if(rc.setting >= 11)
				rc.setting = 0;
			if(par4EntityPlayer.getEntityWorld().isRemote)
				par4EntityPlayer.sendMessage(new TextComponentString(I18n.translateToLocal("essentialcraft.txt.redstone_"+rc.setting)));
		}
		else {
			TileMRUCUECRedstoneController rc = (TileMRUCUECRedstoneController)par1World.getTileEntity(par2);
			if(par4EntityPlayer.getEntityWorld().isRemote)
				par4EntityPlayer.sendMessage(new TextComponentString(I18n.translateToLocal("essentialcraft.txt.redstone_"+rc.setting)));
		}
		return true;
	}

	@Override
	public boolean canProvidePower(IBlockState s) {
		return true;
	}

	@Override
	public int getWeakPower(IBlockState s, IBlockAccess w, BlockPos p, EnumFacing f){
		TileMRUCUECRedstoneController rc = (TileMRUCUECRedstoneController)w.getTileEntity(p);

		return rc.outputRedstone() ? 15 : 0;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState s)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("essentialcraft:ecredstonecontroller", "inventory"));
	}
}
