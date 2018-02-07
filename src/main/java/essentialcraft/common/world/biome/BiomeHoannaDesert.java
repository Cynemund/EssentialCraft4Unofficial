package essentialcraft.common.world.biome;

import java.util.Random;

import essentialcraft.common.world.gen.WorldGenDreadCacti;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class BiomeHoannaDesert extends Biome {

	public int grassColor = 16777215;
	public int waterColor = 16777215;
	public int leavesColor = 16777215;

	public BiomeHoannaDesert setGrassColor(int i) {
		grassColor = i;
		return this;
	}

	public BiomeHoannaDesert setWaterColor(int i) {
		waterColor = i;
		return this;
	}

	public BiomeHoannaDesert setLeavesColor(int i) {
		leavesColor = i;
		return this;
	}

	public BiomeHoannaDesert(BiomeProperties par1) {
		super(par1);
		this.topBlock = Blocks.SAND.getDefaultState();
		this.fillerBlock = Blocks.SAND.getDefaultState();
		this.decorator.treesPerChunk = -999;
		this.decorator.deadBushPerChunk = 2;
		this.decorator.reedsPerChunk = 50;
		this.decorator.cactiPerChunk = 10;
		this.decorator.cactusGen = new WorldGenDreadCacti();
		this.spawnableCreatureList.clear();
	}

	@Override
	public int getGrassColorAtPos(BlockPos pos) {
		return grassColor;
	}

	@Override
	public int getFoliageColorAtPos(BlockPos pos) {
		return leavesColor;
	}

	@Override
	public int getWaterColorMultiplier() {
		return waterColor;
	}

	@Override
	public int getModdedBiomeGrassColor(int original) {
		return grassColor;
	}

	@Override
	public int getModdedBiomeFoliageColor(int original) {
		return leavesColor;
	}

	@Override
	public void decorate(World world, Random rand, BlockPos pos) {
		this.decorator.decorate(world, rand, this, pos);
	}
}
