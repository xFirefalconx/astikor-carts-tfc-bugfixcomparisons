package de.mennomax.astikorcarts.common.items;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.util.Helpers;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import de.mennomax.astikorcarts.AstikorCarts;
import de.mennomax.astikorcarts.item.CartItem;

import static de.mennomax.astikorcarts.common.AstikorItemGroup.*;

@SuppressWarnings("unused")
public class AstikorItems
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AstikorCarts.ID);

    public static final Map<Wood, RegistryObject<Item>> WHEEL_TFC = Helpers.mapOfKeys(Wood.class, wood -> register("wheel/" + wood.name(), CARTS));
    public static final Map<Wood, RegistryObject<Item>> SUPPLY_CART_TFC = Helpers.mapOfKeys(Wood.class, wood -> register("supply_cart/" + wood.name(), () -> new CartItem(new Item.Properties().stacksTo(1).tab(CARTS))));
    public static final Map<Wood, RegistryObject<Item>> PLOW_TFC = Helpers.mapOfKeys(Wood.class, wood -> register("plow/" + wood.name(), () -> new CartItem(new Item.Properties().stacksTo(1).tab(CARTS))));
    public static final Map<Wood, RegistryObject<Item>> ANIMAL_CART_TFC = Helpers.mapOfKeys(Wood.class, wood -> register("animal_cart/" + wood.name(), () -> new CartItem(new Item.Properties().stacksTo(1).tab(CARTS))));
    public static final Map<Wood, RegistryObject<Item>> POSTILION_TFC = Helpers.mapOfKeys(Wood.class, wood -> register("postilion/" + wood.name(), () -> new CartItem(new Item.Properties().stacksTo(1))));

    private static Item.Properties prop()
    {
        return new Item.Properties().tab(CARTS);
    }

    private static RegistryObject<Item> register(String name, CreativeModeTab group)
    {
        return register(name, () -> new Item(new Item.Properties().tab(group)));
    }

    private static <T extends Item> RegistryObject<T> register(String name, Supplier<T> item)
    {
        //TFCFlorae.LOGGER.warn("\"item." + TFCFlorae.MOD_ID + "." + name.toLowerCase() + "\":" );
        return ITEMS.register(name.toLowerCase(Locale.ROOT), item);
    }
}
