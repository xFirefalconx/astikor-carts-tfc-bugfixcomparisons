package de.mennomax.astikorcarts.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerPattern;

import java.util.List;

import de.mennomax.astikorcarts.AstikorCarts;
import de.mennomax.astikorcarts.client.AstikorRenderHelpers;
import de.mennomax.astikorcarts.client.renderer.AstikorCartsModelLayers;
import de.mennomax.astikorcarts.client.renderer.entity.model.AnimalCartModel;
import de.mennomax.astikorcarts.entity.AnimalCartEntity;
import de.mennomax.astikorcarts.util.AstikorHelpers;

public final class AnimalCartRenderer extends DrawnRenderer<AnimalCartEntity, AnimalCartModel> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(AstikorCarts.ID, "textures/entity/animal_cart.png");
    private final Pair<ResourceLocation, AnimalCartModel> location;

    public static ModelLayerLocation entityName(String name)
    {
        return AstikorRenderHelpers.modelIdentifier("animal_cart/" + name);
    }

    public AnimalCartRenderer(final EntityRendererProvider.Context renderManager, String name) {
        super(renderManager, new AnimalCartModel(renderManager.bakeLayer(entityName(name))));
        this.shadowRadius = 1.0F;
        this.location = Pair.of(AstikorHelpers.identifier("textures/entity/animal_cart/" + name + ".png"), new AnimalCartModel(renderManager.bakeLayer(entityName(name))));
    }

    @Override
    protected void renderContents(final AnimalCartEntity entity, final float delta, final PoseStack stack, final MultiBufferSource source, final int packedLight) {
        super.renderContents(entity, delta, stack, source, packedLight);
        final List<Pair<BannerPattern, DyeColor>> list = entity.getBannerPattern();
        if (!list.isEmpty()) {
            stack.pushPose();
            this.model.getBody().translateAndRotate(stack);
            stack.translate(0.0D, -0.6D, 1.56D);
            this.renderBanner(stack, source, packedLight, list);
            stack.popPose();
        }
    }

    @Override
    public ResourceLocation getTextureLocation(final AnimalCartEntity entity) {
        return getModelWithLocation(entity).getFirst();
    }

    public Pair<ResourceLocation, AnimalCartModel> getModelWithLocation(AnimalCartEntity entity)
    {
        return location;
    }
}
