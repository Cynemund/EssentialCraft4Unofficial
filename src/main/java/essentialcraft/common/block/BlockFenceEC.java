package essentialcraft.common.block;

import DummyCore.Client.IModelRegisterer;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class BlockFenceEC extends BlockFence implements IModelRegisterer {

	public BlockFenceEC(Material p_i45394_1_) {
		super(p_i45394_1_, p_i45394_1_.getMaterialMapColor());
	}

	public BlockFenceEC() {
		super(Material.ROCK, Material.ROCK.getMaterialMapColor());
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("essentialcraft:" + getRegistryName().getResourcePath(), "inventory"));
	}
}
