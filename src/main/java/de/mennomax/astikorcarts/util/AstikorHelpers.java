package de.mennomax.astikorcarts.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import static de.mennomax.astikorcarts.AstikorCarts.ID;

public class AstikorHelpers
{
    public static ResourceLocation identifier(String id)
    {
        return new ResourceLocation(ID, id);
    }

    public static Component blockEntityName(String name)
    {
        return new TranslatableComponent(ID + ".block_entity." + name);
    }
}
