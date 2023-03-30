package de.mennomax.astikorcarts.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Vector3f;
import de.mennomax.astikorcarts.AstikorCarts;
import de.mennomax.astikorcarts.client.AstikorRenderHelpers;
import de.mennomax.astikorcarts.client.renderer.AstikorCartsModelLayers;
import de.mennomax.astikorcarts.client.renderer.entity.model.PlowModel;
import de.mennomax.astikorcarts.entity.PlowEntity;
import de.mennomax.astikorcarts.util.AstikorHelpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

public final class PlowRenderer extends DrawnRenderer<PlowEntity, PlowModel> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(AstikorCarts.ID, "textures/entity/plow.png");
    private final Pair<ResourceLocation, PlowModel> location;

    public static ModelLayerLocation entityName(String name)
    {
        return AstikorRenderHelpers.modelIdentifier("plow/" + name);
    }

    public PlowRenderer(final EntityRendererProvider.Context renderManager, String name) {
        super(renderManager, new PlowModel(renderManager.bakeLayer(entityName(name))));
        this.shadowRadius = 1.0F;
        this.location = Pair.of(AstikorHelpers.identifier("textures/entity/plow/" + name + ".png"), new PlowModel(renderManager.bakeLayer(entityName(name))));
    }

    @Override
    public ResourceLocation getTextureLocation(final PlowEntity entity) {
        return getModelWithLocation(entity).getFirst();
    }

    public Pair<ResourceLocation, PlowModel> getModelWithLocation(PlowEntity entity)
    {
        return location;
    }

    @Override
    protected void renderContents(final PlowEntity entity, final float delta, final PoseStack stack, final MultiBufferSource source, final int packedLight) {
        super.renderContents(entity, delta, stack, source, packedLight);
        for (int i = 0; i < entity.inventory.getSlots(); i++) {
            final ItemStack itemStack = entity.getStackInSlot(i);
            if (itemStack.isEmpty()) {
                continue;
            }
            this.attach(this.model.getBody(), this.model.getShaft(i), s -> {
                s.mulPose(Vector3f.XP.rotationDegrees(-90.0F));
                s.mulPose(Vector3f.YP.rotationDegrees(90.0F));
                s.translate(-4.0D / 16.0D, 1.0D / 16.0D, 0.0D);
                if (itemStack.getItem() instanceof BlockItem) {
                    s.translate(0.0D, -0.1D, 0.0D);
                    s.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
                }
                Minecraft.getInstance().getItemRenderer().renderStatic(itemStack, ItemTransforms.TransformType.FIXED, packedLight, OverlayTexture.NO_OVERLAY, s, source, 0);
            }, stack);
        }
    }
}
