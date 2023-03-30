package de.mennomax.astikorcarts.common.entities;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.GlowSquid;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import de.mennomax.astikorcarts.common.items.AstikorItems;
import de.mennomax.astikorcarts.entity.AnimalCartEntity;
import de.mennomax.astikorcarts.entity.PlowEntity;
import de.mennomax.astikorcarts.entity.PostilionEntity;
import de.mennomax.astikorcarts.entity.SupplyCartEntity;

import net.dries007.tfc.client.TFCSounds;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.Helpers;

import static de.mennomax.astikorcarts.AstikorCarts.ID;

@SuppressWarnings("unused")
public class AstikorEntities
{
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, ID);

    public static final Map<Wood, RegistryObject<EntityType<SupplyCartEntity>>> SUPPLY_CART_TFC = Helpers.mapOfKeys(Wood.class, wood ->
        register("supply_cart/" + wood.name(), EntityType.Builder.<SupplyCartEntity>of((type, level) -> new SupplyCartEntity(type, level, AstikorItems.SUPPLY_CART_TFC.get(wood)), MobCategory.MISC).sized(1.5F, 1.4F).clientTrackingRange(10))
    );
    public static final Map<Wood, RegistryObject<EntityType<PlowEntity>>> PLOW_TFC = Helpers.mapOfKeys(Wood.class, wood ->
        register("plow/" + wood.name(), EntityType.Builder.<PlowEntity>of((type, level) -> new PlowEntity(type, level, AstikorItems.PLOW_TFC.get(wood)), MobCategory.MISC).sized(1.3F, 1.4F).clientTrackingRange(10))
    );
    public static final Map<Wood, RegistryObject<EntityType<AnimalCartEntity>>> ANIMAL_CART_TFC = Helpers.mapOfKeys(Wood.class, wood ->
        register("animal_cart/" + wood.name(), EntityType.Builder.<AnimalCartEntity>of((type, level) -> new AnimalCartEntity(type, level, AstikorItems.ANIMAL_CART_TFC.get(wood), wood), MobCategory.MISC).sized(1.3F, 1.4F).clientTrackingRange(10))
    );
    public static final Map<Wood, RegistryObject<EntityType<PostilionEntity>>> POSTILION_TFC = Helpers.mapOfKeys(Wood.class, wood ->
        register("postilion/" + wood.name(), EntityType.Builder.<PostilionEntity>of((type, level) -> new PostilionEntity(type, level, AstikorItems.POSTILION_TFC.get(wood)), MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(10))
    );

    public static <E extends Entity> RegistryObject<EntityType<E>> register(String name, EntityType.Builder<E> builder)
    {
        return register(name, builder, true);
    }

    public static <E extends Entity> RegistryObject<EntityType<E>> register(String name, EntityType.Builder<E> builder, boolean serialize)
    {
        final String id = name.toLowerCase(Locale.ROOT);
        return ENTITIES.register(id, () -> builder.build(ID + ":" + id));
    }

    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event)
    {
    }
}
