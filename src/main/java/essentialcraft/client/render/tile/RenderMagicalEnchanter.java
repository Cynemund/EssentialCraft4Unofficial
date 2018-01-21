package essentialcraft.client.render.tile;

import essentialcraft.common.tile.TileMagicalEnchanter;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMagicalEnchanter extends TileEntitySpecialRenderer<TileMagicalEnchanter> {

	private static final ResourceLocation TEXTURE_BOOK = new ResourceLocation("textures/entity/enchanting_table_book.png");
	private final ModelBook modelBook = new ModelBook();

	@Override
	public void render(TileMagicalEnchanter te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5F, y + 0.75F, z + 0.5F);
		float f = te.tickCount + partialTicks;
		GlStateManager.translate(0.0F, 0.1F + MathHelper.sin(f * 0.1F) * 0.01F, 0.0F);
		float f1;

		for(f1 = te.bookRotation - te.bookRotationPrev; f1 >= Math.PI; f1 -= Math.PI * 2F) {

		}

		while(f1 < -Math.PI) {
			f1 += Math.PI * 2F;
		}

		float f2 = te.bookRotationPrev + f1 * partialTicks;
		GlStateManager.rotate((float)(-f2 * (180F / Math.PI)), 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(80.0F, 0.0F, 0.0F, 1.0F);
		this.bindTexture(TEXTURE_BOOK);
		float f3 = te.pageFlipPrev + (te.pageFlip - te.pageFlipPrev) * partialTicks + 0.25F;
		float f4 = te.pageFlipPrev + (te.pageFlip - te.pageFlipPrev) * partialTicks + 0.75F;
		f3 = (f3 - MathHelper.fastFloor(f3)) * 1.6F - 0.3F;
		f4 = (f4 - MathHelper.fastFloor(f4)) * 1.6F - 0.3F;

		if(f3 < 0.0F) {
			f3 = 0.0F;
		}

		if(f4 < 0.0F) {
			f4 = 0.0F;
		}

		if(f3 > 1.0F) {
			f3 = 1.0F;
		}

		if(f4 > 1.0F) {
			f4 = 1.0F;
		}

		float f5 = te.bookSpreadPrev + (te.bookSpread - te.bookSpreadPrev) * partialTicks;
		GlStateManager.enableCull();
		this.modelBook.render(null, f, f3, f4, f5, 0.0F, 0.0625F);
		GlStateManager.popMatrix();
	}
}