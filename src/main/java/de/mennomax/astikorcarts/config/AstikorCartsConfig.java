package de.mennomax.astikorcarts.config;

import net.dries007.tfc.common.capabilities.size.Size;
import net.jodah.typetools.TypeResolver;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ItemSteerable;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AstikorCartsConfig
{

    public static Common get()
    {
        return Holder.COMMON;
    }

    public static ForgeConfigSpec spec()
    {
        return Holder.COMMON_SPEC;
    }

    private static final class Holder
    {
        private static final Common COMMON;

        private static final ForgeConfigSpec COMMON_SPEC;

        static
        {
            final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
            COMMON = specPair.getLeft();
            COMMON_SPEC = specPair.getRight();
        }
    }

    public static class Common
    {
        public final CartConfig supplyCart;
        public final CartConfig animalCart;
        public final CartConfig plow;
        public final MiscConfig config;

        Common(final ForgeConfigSpec.Builder builder)
        {
            builder.comment("Configuration for all carts and cart-like vehicles\n\nDefault pull_animals = " + referencePullAnimals()).push("carts");
            this.supplyCart = new CartConfig(builder, "supply_cart", "The Supply Cart, a type of cart that stores items");
            this.animalCart = new CartConfig(builder, "animal_cart", "The Animal Cart, a type of cart to haul other animals");
            this.plow = new CartConfig(builder, "plow", "The Plow, an animal pulled machine for tilling soil and creating paths");
            this.config = new MiscConfig(builder, "config", "Miscellaneous config options");
            builder.pop();
        }

        static String referencePullAnimals()
        {
            return "[\n" +
                StreamSupport.stream(ForgeRegistries.ENTITIES.spliterator(), false)
                    .filter(type -> {
                        final Class<?> entityClass = TypeResolver.resolveRawArgument(EntityType.EntityFactory.class, Objects.requireNonNull(
                            ObfuscationReflectionHelper.getPrivateValue(EntityType.class, type, "f_20535_"),
                            "factory"
                        ).getClass());
                        if (Entity.class.equals(entityClass)) return type == EntityType.PLAYER;
                        return Saddleable.class.isAssignableFrom(entityClass) &&
                            !ItemSteerable.class.isAssignableFrom(entityClass) &&
                            !Llama.class.isAssignableFrom(entityClass); // no horse-llamas
                    })
                    .map(ForgeRegistryEntry::getRegistryName)
                    .filter(Objects::nonNull)
                    .map(type -> "    \"" + type.toString() + "\"")
                    .collect(Collectors.joining(",\n")) +
                "\n  ]";
        }
    }

    public static class CartConfig
    {
        public final ForgeConfigSpec.ConfigValue<ArrayList<String>> pullAnimals;
        public final ForgeConfigSpec.DoubleValue slowSpeed;
        public final ForgeConfigSpec.DoubleValue pullSpeed;

        CartConfig(final ForgeConfigSpec.Builder builder, final String name, final String description)
        {
            builder.comment(description).push(name);
            this.pullAnimals = builder
                .comment(
                    "Animals that are able to pull this cart, such as [\"minecraft:horse\"]\n" +
                    "An empty list defaults to all which may wear a saddle but not steered by an item"
                )
                .define("pull_animals", new ArrayList<>());
            this.slowSpeed = builder.comment("Slow speed modifier toggled by the sprint key")
                .defineInRange("slow_speed", -0.65D, -1.0D, 0.0D);
            this.pullSpeed = builder.comment("Base speed modifier applied to animals (-0.5 = half normal speed)")
                .defineInRange("pull_speed", 0.0D, -1.0D, 0.0D);
            builder.pop();
        }
    }

    public static class MiscConfig
    {
        public final ForgeConfigSpec.IntValue maxAnimalSize;
        public final ForgeConfigSpec.EnumValue<Size> maxItemSize;
        public final ForgeConfigSpec.BooleanValue canPushIntoPlayers;
        public final ForgeConfigSpec.BooleanValue canCarryWaterEntities;

        MiscConfig(final ForgeConfigSpec.Builder builder, final String name, final String description)
        {
            maxAnimalSize = builder.comment("Max animal size that the animal cart can carry.").defineInRange("maxAnimalSize", 2, 0, 69);
            maxItemSize = builder.comment("The largest (inclusive) size of an item that is allowed in a supply cart.").defineEnum("maxItemSize", Size.VERY_LARGE);
            canPushIntoPlayers = builder.comment("Can the animal cart pick up players by pushing it into them?").define("canPushIntoPlayers", true);
            canCarryWaterEntities = builder.comment("Can the animal cart pick up water animals?").define("canCarryWaterEntities", true);
        }
    }
}
