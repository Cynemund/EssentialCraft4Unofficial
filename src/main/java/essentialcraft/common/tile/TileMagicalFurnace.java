package essentialcraft.common.tile;

import java.util.List;
import java.util.function.BiPredicate;

import DummyCore.Utils.BiPredicates;
import DummyCore.Utils.DataStorage;
import DummyCore.Utils.DummyData;
import DummyCore.Utils.MathUtils;
import essentialcraft.api.ApiCore;
import essentialcraft.common.block.BlocksCore;
import essentialcraft.common.item.ItemsCore;
import essentialcraft.common.mod.EssentialCraftCore;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.config.Configuration;

public class TileMagicalFurnace extends TileMRUGeneric {
	public int progressLevel;
	public static int cfgMaxMRU = ApiCore.DEVICE_MAX_MRU_GENERIC*10;
	public static int mruUsage = 25;
	public static int smeltingTime = 20;
	public static float chanceToDoubleOutput = 0.3F;
	public static float chanceToDoubleSlags = 0.1F;

	public TileMagicalFurnace() {
		super();
		mruStorage.setMaxMRU(cfgMaxMRU);
		setSlotsNum(1);
	}

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		progressLevel = par1NBTTagCompound.getInteger("progress");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("progress", progressLevel);
		return par1NBTTagCompound;
	}

	@Override
	public void update() {
		super.update();
		spawnParticles();
		mruStorage.update(getPos(), getWorld(), getStackInSlot(0));
		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0)
			smelt();
	}

	protected static boolean testBlock(IBlockAccess world, BlockPos pos, Block block) {
		return world.getBlockState(pos).getBlock() == block;
	}

	protected static BiPredicate<IBlockAccess, BlockPos> structureChecker = BiPredicates.<IBlockAccess, BlockPos>and(
			(world, pos)->{
				for(int x = -2; x <= 2; ++x) {
					for(int z = -2; z <= 2; ++z) {
						if(!testBlock(world, pos.add(x, -1, z), BlocksCore.voidStone)) {
							return false;
						}
					}
				}
				return true;
			}, (world, pos)->
			testBlock(world, pos.add( 2, 0, 2), BlocksCore.voidStone) &&
			testBlock(world, pos.add(-2, 0, 2), BlocksCore.voidStone) &&
			testBlock(world, pos.add( 2, 0,-2), BlocksCore.voidStone) &&
			testBlock(world, pos.add(-2, 0,-2), BlocksCore.voidStone) &&
			testBlock(world, pos.add( 2, 1, 2), BlocksCore.heatGenerator) &&
			testBlock(world, pos.add(-2, 1, 2), BlocksCore.heatGenerator) &&
			testBlock(world, pos.add( 2, 1,-2), BlocksCore.heatGenerator) &&
			testBlock(world, pos.add(-2, 1,-2), BlocksCore.heatGenerator)
			);

	public void smelt() {
		EntityItem smeltingItem = getSmeltingItem();
		ItemStack smeltingStack = smeltingItem == null ? ItemStack.EMPTY : smeltingItem.getItem();
		if(structureChecker.test(getWorld(), getPos()) && mruStorage.getMRU() >= mruUsage && !smeltingStack.isEmpty() && smeltingItem != null) {
			if(progressLevel == 0) {
				getWorld().playSound(null, pos, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F, 1);
			}
			ItemStack mainSmelting = smeltingStack.copy();
			++progressLevel;
			mruStorage.extractMRU(mruUsage, true);
			if(progressLevel >= smeltingTime) {
				progressLevel = 0;
				getWorld().playSound(null, pos, SoundEvents.BLOCK_LAVA_POP, SoundCategory.BLOCKS, 1.0F, 1);
				ItemStack s = FurnaceRecipes.instance().getSmeltingResult(mainSmelting).copy();
				if(!getWorld().isRemote) {
					smeltingItem.getItem().shrink(1);
					if(smeltingItem.getItem().getCount() <= 0) {
						smeltingItem.setDead();
					}
				}
				if(!getWorld().isRemote && getWorld().rand.nextFloat() <= chanceToDoubleOutput) {
					EntityItem smelted = new EntityItem(getWorld(), pos.getX()-1.5, pos.getY()+2.15, pos.getZ()-1.5, s.copy());
					smelted.motionX = 0;
					smelted.motionY = 0;
					smelted.motionZ = 0;
					getWorld().spawnEntity(smelted);
				}
				if(!getWorld().isRemote) {
					EntityItem smelted = new EntityItem(getWorld(),pos.getX()+2.5, pos.getY()+2.15, pos.getZ()+2.5, s.copy());
					smelted.motionX = 0;
					smelted.motionY = 0;
					smelted.motionZ = 0;
					getWorld().spawnEntity(smelted);
				}
				if(!getWorld().isRemote && getWorld().rand.nextFloat() <= chanceToDoubleSlags) {
					EntityItem slag = new EntityItem(getWorld(),pos.getX()-1.5, pos.getY()+2.15, pos.getZ()+2.5, new ItemStack(ItemsCore.magicalSlag));
					slag.motionX = 0;
					slag.motionY = 0;
					slag.motionZ = 0;
					getWorld().spawnEntity(slag);
				}
				if(!getWorld().isRemote) {
					EntityItem slag = new EntityItem(getWorld(),pos.getX()+2.5,pos.getY()+2.15,pos.getZ()-1.5,new ItemStack(ItemsCore.magicalSlag));
					slag.motionX = 0;
					slag.motionY = 0;
					slag.motionZ = 0;
					getWorld().spawnEntity(slag);
				}
			}
		}
		else
			progressLevel = 0;
	}

	public EntityItem getSmeltingItem() {
		EntityItem ret = null;
		List<EntityItem> l = getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX()+1, pos.getY()+2, pos.getZ()+1));
		for(EntityItem item : l) {
			if(!FurnaceRecipes.instance().getSmeltingResult(item.getItem()).isEmpty())
				return item;
		}
		return ret;
	}

	public void spawnParticles() {
		if(getWorld().isRemote && structureChecker.test(getWorld(), getPos())) {
			/*for(int i = 0; i < 100; ++i) */ {
				EssentialCraftCore.proxy.spawnParticle("cSpellFX", pos.getX()+0.5F + MathUtils.randomFloat(getWorld().rand)*3, pos.getY(), pos.getZ()+0.5F + MathUtils.randomFloat(getWorld().rand)*3, 0,2, 0);
			}
		}
	}

	public static void setupConfig(Configuration cfg) {
		try {
			cfg.load();
			String[] cfgArrayString = cfg.getStringList("MagicalFurnaceSettings", "tileentities", new String[] {
					"Max MRU:" + ApiCore.DEVICE_MAX_MRU_GENERIC*10,
					"MRU Usage:25",
					"Ticks required to smelt 1 item:20",
					"Chance to double the outcome:0.3",
					"Chance to double slags outcome:0.1"
			}, "");
			String dataString = "";

			for(int i = 0; i < cfgArrayString.length; ++i)
				dataString += "||" + cfgArrayString[i];

			DummyData[] data = DataStorage.parseData(dataString);

			mruUsage = Integer.parseInt(data[1].fieldValue);
			smeltingTime = Integer.parseInt(data[2].fieldValue);
			cfgMaxMRU = Integer.parseInt(data[0].fieldValue);
			chanceToDoubleOutput = Float.parseFloat(data[3].fieldValue);
			chanceToDoubleSlags = Float.parseFloat(data[4].fieldValue);

			cfg.save();
		}
		catch(Exception e) {
			return;
		}
	}

	@Override
	public int[] getOutputSlots() {
		return new int[0];
	}
}
