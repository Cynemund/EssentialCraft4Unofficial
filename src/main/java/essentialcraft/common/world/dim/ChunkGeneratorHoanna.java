package essentialcraft.common.world.dim;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import essentialcraft.common.world.gen.structure.MapGenModernShafts;
import essentialcraft.common.world.gen.structure.MapGenOldCatacombs;
import essentialcraft.common.world.gen.structure.MapGenTown;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkGeneratorOverworld;

public class ChunkGeneratorHoanna extends ChunkGeneratorOverworld {

	public Random rand;
	public World world;
	public MapGenTown town = new MapGenTown();
	public MapGenModernShafts shafts = new MapGenModernShafts();
	public MapGenOldCatacombs catacombs = new MapGenOldCatacombs();
	public static ChunkGeneratorHoanna instance;

	public ChunkGeneratorHoanna(World world, long seed, boolean mapFeaturesEnabledIn, String generatorOptions) {
		super(world, seed, false, generatorOptions);
		instance = this;
		this.rand = new Random(seed);
		this.world = world;
	}

	@Override
	public void populate(int chunkX, int chunkZ) {
		instance = this;
		this.town.generateStructure(this.world, this.rand, new ChunkPos(chunkX, chunkZ));
		this.shafts.generateStructure(this.world, this.rand, new ChunkPos(chunkX, chunkZ));
		this.catacombs.generateStructure(this.world, this.rand, new ChunkPos(chunkX, chunkZ));
		super.populate(chunkX, chunkZ);
	}

	@Override
	public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos) {
		if(structureName.equals("Town")) {
			return this.town.isInsideStructure(pos);
		}
		if(structureName.equals("ModernShafts")) {
			return this.shafts.isInsideStructure(pos);
		}
		if(structureName.equals("OldCatacombs")) {
			return this.catacombs.isInsideStructure(pos);
		}
		return super.isInsideStructure(worldIn, structureName, pos);
	}

	@Override
	public Chunk generateChunk(int chunkX, int chunkZ) {
		instance = this;
		ChunkPrimer chunkprimer = new ChunkPrimer();
		this.town.generate(this.world, chunkX, chunkZ, chunkprimer);
		this.shafts.generate(this.world, chunkX, chunkZ, chunkprimer);
		this.catacombs.generate(this.world, chunkX, chunkZ, chunkprimer);
		return super.generateChunk(chunkX, chunkZ);
	}

	@Override
	public void recreateStructures(Chunk chunk, int chunkX, int chunkZ) {
		ChunkPrimer chunkprimer = new ChunkPrimer();
		this.town.generate(this.world, chunkX, chunkZ, chunkprimer);
		this.shafts.generate(this.world, chunkX, chunkZ, chunkprimer);
		this.catacombs.generate(this.world, chunkX, chunkZ, chunkprimer);
		super.recreateStructures(chunk, chunkX, chunkZ);
	}

	@Override
	public List<SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
		if(creatureType.getPeacefulCreature()) {
			return Lists.<SpawnListEntry>newArrayList();
		}
		return super.getPossibleCreatures(creatureType, pos);
	}
}
