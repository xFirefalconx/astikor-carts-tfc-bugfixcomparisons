package de.mennomax.astikorcarts.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Vector3f;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;

import de.mennomax.astikorcarts.client.AstikorRenderHelpers;
import de.mennomax.astikorcarts.client.renderer.entity.model.SupplyCartModel;
import de.mennomax.astikorcarts.entity.PostilionEntity;
import de.mennomax.astikorcarts.util.AstikorHelpers;

public final class PostilionRenderer extends EntityRenderer<PostilionEntity> {
    //private final Pair<ResourceLocation, SupplyCartModel> location;

    public static ModelLayerLocation entityName(String name)
    {
        return AstikorRenderHelpers.modelIdentifier("postilion/" + name);
    }

    public PostilionRenderer(final EntityRendererProvider.Context manager, String name) {
        super(manager);
        //this.location = Pair.of(AstikorHelpers.identifier("textures/entity/postilion/" + name + ".png"), new SupplyCartModel(manager.bakeLayer(entityName(name))));
    }

    @Override
    public void render(final PostilionEntity postilion, final float yaw, final float delta, final PoseStack stack, final MultiBufferSource source, final int packedLight) {
        if (!postilion.isInvisible()) {
            stack.pushPose();
            stack.mulPose(Vector3f.YP.rotationDegrees(180.0F - yaw));
            final AABB bounds = postilion.getBoundingBox().move(-postilion.getX(), -postilion.getY(), -postilion.getZ());
            LevelRenderer.renderLineBox(stack, source.getBuffer(RenderType.lines()), bounds, 1.0F, 1.0F, 1.0F, 1.0F);
            stack.popPose();
            super.render(postilion, yaw, delta, stack, source, packedLight);
        }
    }

    @Override
    protected boolean shouldShowName(final PostilionEntity postilion) {
        return true;
    }

    @Nullable
    @Override
    public ResourceLocation getTextureLocation(final PostilionEntity postilion) {
        return null;
    }
}
