package essentialcraft.common.mod;

import java.util.Arrays;

import DummyCore.Core.Core;
import essentialcraft.common.block.BlocksCore;
import essentialcraft.common.entity.EntitiesCore;
import essentialcraft.common.item.ItemsCore;
import essentialcraft.common.registry.BiomeRegistry;
import essentialcraft.common.registry.CERegistry;
import essentialcraft.common.registry.CapabilityRegistry;
import essentialcraft.common.registry.CoreRegistry;
import essentialcraft.common.registry.DimensionRegistry;
import essentialcraft.common.registry.EnchantRegistry;
import essentialcraft.common.registry.GunInitialization;
import essentialcraft.common.registry.OreDictionaryRegistry;
import essentialcraft.common.registry.PotionRegistry;
import essentialcraft.common.registry.RecipesCore;
import essentialcraft.common.registry.ResearchRegistry;
import essentialcraft.common.registry.SoundRegistry;
import essentialcraft.common.registry.StructureRegistry;
import essentialcraft.common.registry.TileRegistry;
import essentialcraft.common.registry.VillagersRegistry;
import essentialcraft.common.world.gen.WorldGenManager;
import essentialcraft.integration.bloodmagic.BloodMagicRegistry;
import essentialcraft.integration.tconstruct.TConstructRegistry;
import essentialcraft.integration.waila.WailaInitializer;
import essentialcraft.proxy.CommonProxy;
import essentialcraft.utils.cfg.Config;
import essentialcraft.utils.commands.CommandCorruptionEffect;
import essentialcraft.utils.commands.CommandCreateMRUCU;
import essentialcraft.utils.commands.CommandHoannaEvent;
import essentialcraft.utils.commands.CommandSetBalanceInMRUCU;
import essentialcraft.utils.commands.CommandSetMRUInMRUCU;
import essentialcraft.utils.commands.CommandSetUBMRU;
import essentialcraft.utils.common.ECEventHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(
		modid = EssentialCraftCore.MODID,
		name = "EssentialCraftIVUnofficial",
		version = EssentialCraftCore.VERSION,
		dependencies = "required-after:dummycore@[2.4.112.3,);required-after:baubles",
		guiFactory = "essentialcraft.client.gui.ModConfigGuiHandler"
		)
public class EssentialCraftCore {

	//============================================CORE VARS==================================================//
	@Instance(EssentialCraftCore.MODID)
	public static EssentialCraftCore core;
	@SidedProxy(clientSide = "essentialcraft.proxy.ClientProxy", serverSide = "essentialcraft.proxy.CommonProxy", modId = EssentialCraftCore.MODID)
	public static CommonProxy proxy;
	public static Config cfg = new Config();
	public static final String VERSION = "4.9.112.3";
	public static final String MODID = "essentialcraft";
	public static ModMetadata metadata;
	public static SimpleNetworkWrapper network;
	public static final boolean UNOFFICIAL = true;
	//============================================CORE FUNCTIONS=============================================//
	//============================================CORE MOD===================================================//
	@EventHandler
	public void serverStart(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandSetMRUInMRUCU());
		event.registerServerCommand(new CommandSetBalanceInMRUCU());
		event.registerServerCommand(new CommandCreateMRUCU());
		event.registerServerCommand(new CommandSetUBMRU());
		event.registerServerCommand(new CommandHoannaEvent());
		event.registerServerCommand(new CommandCorruptionEffect());
	}

	@EventHandler
	public void firstMovement(FMLPreInitializationEvent event) {
		metadata = event.getModMetadata();

		core = this;
		try {
			Core.registerModAbsolute(getClass(), "EssentialCraft", event.getModConfigurationDirectory().getAbsolutePath(), cfg);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		proxy.firstMovement(event);

		CapabilityRegistry.register();
		WailaInitializer.sendIMC();

		CoreRegistry.register();
		SoundRegistry.init();

		BlocksCore.loadBlocks();
		ItemsCore.loadItems();
		BlocksCore.postInitLoad();
		TileRegistry.register();

		EntitiesCore.registerEntities();
		proxy.registerRenderInformation();

		PotionRegistry.registerPotions(ForgeRegistries.POTIONS);

		DimensionRegistry.register();
		BiomeRegistry.register(ForgeRegistries.BIOMES);
		VillagersRegistry.register();
		EnchantRegistry.register();

		TConstructRegistry.register();
		OreDictionaryRegistry.register();
		GunInitialization.register();
	}

	@EventHandler
	public void secondMovement(FMLInitializationEvent event) {
		RecipesCore.main();

		StructureRegistry.register();
		proxy.registerTileEntitySpecialRenderer();

		CERegistry.register();
		ECEventHandler.shouldLoadLoot = true;
	}

	public static boolean clazzExists(String clazzName) {
		try {
			Class<?> clazz = Class.forName(clazzName);
			return clazz != null;
		}
		catch(Exception e) {
			return false;
		}
	}

	@EventHandler
	public void thirdMovement(FMLPostInitializationEvent event) {
		BloodMagicRegistry.register();
		GameRegistry.registerWorldGenerator(new WorldGenManager(), 16);
		ResearchRegistry.init();

		metadata.autogenerated = false;
		metadata.modId = MODID;
		metadata.version = VERSION;
		metadata.name="Essential Craft 4 Unofficial";
		metadata.credits="Author: Modbder, TheLMiffy1111, mrAppleXZ, InfinityStudio";
		metadata.authorList.addAll(Arrays.<String>asList(new String[]{"Modbder", "TheLMiffy1111", "mrAppleXZ"}));
		metadata.description = "Essential Craft 4 is a modified version of Essential Craft 3, which is a huge magic-themed mod, that adds lots of end-game content.";
		metadata.url = "http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/2286105-1-7-10-forge-open-source-dummythinking-mods";
		metadata.logoFile = "assets/essentialcraft/textures/special/logo.png";
	}
}
