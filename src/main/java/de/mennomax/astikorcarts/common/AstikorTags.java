package de.mennomax.astikorcarts.common;

import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import de.mennomax.astikorcarts.util.AstikorHelpers;

public class AstikorTags
{
    public static class Items
    {
        public static final TagKey<Item> CART_WHEEL = create("cart_wheel");

        private static TagKey<Item> create(String id)
        {
            return TagKey.create(Registry.ITEM_REGISTRY, AstikorHelpers.identifier(id));
        }
    }
}
