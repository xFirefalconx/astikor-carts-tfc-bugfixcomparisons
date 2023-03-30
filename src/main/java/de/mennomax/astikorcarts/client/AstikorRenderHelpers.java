package de.mennomax.astikorcarts.client;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;

import de.mennomax.astikorcarts.util.AstikorHelpers;

public final class AstikorRenderHelpers
{
    @SuppressWarnings("deprecation") public static final ResourceLocation BLOCKS_ATLAS = TextureAtlas.LOCATION_BLOCKS;

    /**
     * Creates {@link ModelLayerLocation} in the default manner
     */
    public static ModelLayerLocation modelIdentifier(String name, String part)
    {
        return new ModelLayerLocation(AstikorHelpers.identifier(name), part);
    }

    public static ModelLayerLocation modelIdentifier(String name)
    {
        return modelIdentifier(name, "main");
    }

    public static ModelPart bakeSimple(EntityRendererProvider.Context ctx, String layerName)
    {
        return ctx.bakeLayer(modelIdentifier(layerName));
    }
}
