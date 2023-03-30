package de.mennomax.astikorcarts;

import de.mennomax.astikorcarts.client.ClientInitializer;
import de.mennomax.astikorcarts.common.entities.*;
import de.mennomax.astikorcarts.common.items.*;
import de.mennomax.astikorcarts.inventory.container.PlowContainer;
import de.mennomax.astikorcarts.network.NetBuilder;
import de.mennomax.astikorcarts.network.clientbound.UpdateDrawnMessage;
import de.mennomax.astikorcarts.network.serverbound.ActionKeyMessage;
import de.mennomax.astikorcarts.network.serverbound.OpenSupplyCartMessage;
import de.mennomax.astikorcarts.network.serverbound.ToggleSlowMessage;
import de.mennomax.astikorcarts.server.ServerInitializer;
import de.mennomax.astikorcarts.util.DefRegister;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.StatType;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

@Mod(AstikorCarts.ID)
public final class AstikorCarts {
    public static final String ID = "astikorcarts";

    public static final SimpleChannel CHANNEL = new NetBuilder(new ResourceLocation(ID, "main"))
        .version(1).optionalServer().requiredClient()
        .serverbound(ActionKeyMessage::new).consumer(() -> ActionKeyMessage::handle)
        .serverbound(ToggleSlowMessage::new).consumer(() -> ToggleSlowMessage::handle)
        .clientbound(UpdateDrawnMessage::new).consumer(() -> new UpdateDrawnMessage.Handler())
        .serverbound(OpenSupplyCartMessage::new).consumer(() -> OpenSupplyCartMessage::handle)
        .build();

    private static final DefRegister REG = new DefRegister(ID);

    public static final class SoundEvents {
        private SoundEvents() {
        }

        private static final DeferredRegister<SoundEvent> R = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ID);

        public static final RegistryObject<SoundEvent> CART_ATTACHED = R.register("entity.cart.attach", () -> new SoundEvent(new ResourceLocation(ID, "entity.cart.attach")));
        public static final RegistryObject<SoundEvent> CART_DETACHED = R.register("entity.cart.detach", () -> new SoundEvent(new ResourceLocation(ID, "entity.cart.detach")));
        public static final RegistryObject<SoundEvent> CART_PLACED = R.register("entity.cart.place", () -> new SoundEvent(new ResourceLocation(ID, "entity.cart.place")));
    }

    public static final class Stats {
        private Stats() {
        }

        private static final DefRegister.Vanilla<ResourceLocation, StatFormatter> R = REG.of(StatType.class, Registry.CUSTOM_STAT, net.minecraft.stats.Stats.CUSTOM::get, rl -> StatFormatter.DEFAULT);

        public static final Supplier<ResourceLocation> CART_ONE_CM = R.make("cart_one_cm", rl -> rl, rl -> StatFormatter.DISTANCE);
    }

    public static final class ContainerTypes {
        private ContainerTypes() {
        }

        private static final DeferredRegister<MenuType<?>> R = DeferredRegister.create(ForgeRegistries.CONTAINERS, ID);

        public static final RegistryObject<MenuType<PlowContainer>> PLOW_CART = R.register("plow", () -> IForgeMenuType.create(PlowContainer::new));
    }

    public AstikorCarts() {
        final Initializer.Context ctx = new InitContext();
        DistExecutor.runForDist(() -> ClientInitializer::new, () -> ServerInitializer::new).init(ctx);
        REG.registerAll(ctx.modBus(), Stats.R);
        AstikorItems.ITEMS.register(ctx.modBus());
        AstikorEntities.ENTITIES.register(ctx.modBus());

        SoundEvents.R.register(ctx.modBus());
        ContainerTypes.R.register(ctx.modBus());
    }

    private static class InitContext implements Initializer.Context {
        @Override
        public ModLoadingContext context() {
            return ModLoadingContext.get();
        }

        @Override
        public IEventBus bus() {
            return MinecraftForge.EVENT_BUS;
        }

        @Override
        public IEventBus modBus() {
            return FMLJavaModLoadingContext.get().getModEventBus();
        }
    }
}
