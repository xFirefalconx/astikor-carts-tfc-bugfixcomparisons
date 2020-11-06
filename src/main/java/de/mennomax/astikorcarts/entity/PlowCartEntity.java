package de.mennomax.astikorcarts.entity;

import java.util.ArrayList;

import com.google.common.collect.ImmutableList;

import de.mennomax.astikorcarts.AstikorCarts;
import de.mennomax.astikorcarts.config.AstikorCartsConfig;
import de.mennomax.astikorcarts.inventory.container.PlowCartContainer;
import de.mennomax.astikorcarts.util.CartItemStackHandler;
import de.mennomax.astikorcarts.util.ProxyItemUseContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.ItemStackHandler;

public final class PlowCartEntity extends AbstractDrawnInventoryEntity {
    private static final int SLOT_COUNT = 3;
    private static final double BLADEOFFSET = 1.7D;
    private static final DataParameter<Boolean> PLOWING = EntityDataManager.createKey(PlowCartEntity.class, DataSerializers.BOOLEAN);
    private static final ImmutableList<DataParameter<ItemStack>> TOOLS = ImmutableList.of(
        EntityDataManager.createKey(PlowCartEntity.class, DataSerializers.ITEMSTACK),
        EntityDataManager.createKey(PlowCartEntity.class, DataSerializers.ITEMSTACK),
        EntityDataManager.createKey(PlowCartEntity.class, DataSerializers.ITEMSTACK));

    public PlowCartEntity(final EntityType<? extends Entity> entityTypeIn, final World worldIn) {
        super(entityTypeIn, worldIn);
        this.spacing = 2.0D;
    }

    @Override
    protected ArrayList<String> getAllowedEntityList() {
        return AstikorCartsConfig.COMMON.plowPullable.get();
    }

    @Override
    protected ItemStackHandler initInventory() {
        return new CartItemStackHandler<PlowCartEntity>(SLOT_COUNT, this) {
            @Override
            protected void onLoad() {
                for (int i = 0; i < TOOLS.size(); i++) {
                    this.cart.getDataManager().set(TOOLS.get(i), this.getStackInSlot(i));
                }
            }

            @Override
            protected void onContentsChanged(final int slot) {
                this.cart.updateSlot(slot);
            }
        };
    }

    public boolean getPlowing() {
        return this.dataManager.get(PLOWING);
    }

    @Override
    public void pulledTick() {
        super.pulledTick();
        if (this.getPulling() == null) {
            return;
        }
        if (!this.world.isRemote) {
            PlayerEntity player = null;
            if (this.getPulling() instanceof PlayerEntity) {
                player = (PlayerEntity) this.getPulling();
            } else if (this.getPulling().getControllingPassenger() instanceof PlayerEntity) {
                player = (PlayerEntity) this.getPulling().getControllingPassenger();
            }
            if (this.dataManager.get(PLOWING) && player != null) {
                if (this.prevPosX != this.getPosX() || this.prevPosZ != this.getPosZ()) {
                    for (int i = 0; i < SLOT_COUNT; i++) {
                        if (!getStackInSlot(i).isEmpty()) {
                            final boolean damageable = getStackInSlot(i).isDamageable();
                            final float offset = 38.0F - i * 38.0F;
                            final double blockPosX = this.getPosX() + MathHelper.sin((this.rotationYaw - offset) * 0.017453292F) * BLADEOFFSET;
                            final double blockPosZ = this.getPosZ() - MathHelper.cos((this.rotationYaw - offset) * 0.017453292F) * BLADEOFFSET;
                            final BlockPos blockPos = new BlockPos(blockPosX, this.getPosY() - 0.5D, blockPosZ);
                            getStackInSlot(i).getItem().onItemUse(new ProxyItemUseContext(player, getStackInSlot(i), new BlockRayTraceResult(Vector3d.ZERO, Direction.UP, blockPos, false)));
                            if (getStackInSlot(i).isEmpty()) {
                                if (damageable) this.playSound(SoundEvents.ENTITY_ITEM_BREAK, 0.8F, 0.8F + this.world.rand.nextFloat() * 0.4F);
                                this.updateSlot(i);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public ActionResultType processInitialInteract(final PlayerEntity player, final Hand hand) {
        if (player.isSecondaryUseActive()) {
            this.openContainer(player);
            return ActionResultType.func_233537_a_(this.world.isRemote);
        }
        if (!this.world.isRemote) {
            this.dataManager.set(PLOWING, !this.dataManager.get(PLOWING));
        }
        return ActionResultType.func_233537_a_(this.world.isRemote);
    }

    public void updateSlot(final int slot) {
        if (!this.world.isRemote) {
            if (this.inventory.getStackInSlot(slot).isEmpty()) {
                this.dataManager.set(TOOLS.get(slot), ItemStack.EMPTY);
            } else {
                this.dataManager.set(TOOLS.get(slot), this.inventory.getStackInSlot(slot));
            }

        }
    }

    public ItemStack getStackInSlot(final int i) {
        return this.dataManager.get(TOOLS.get(i));
    }

    @Override
    public Item getCartItem() {
        return AstikorCarts.Items.PLOW_CART.get();
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(PLOWING, false);
        for (final DataParameter<ItemStack> param : TOOLS) {
            this.dataManager.register(param, ItemStack.EMPTY);
        }
    }

    @Override
    protected void readAdditional(final CompoundNBT compound) {
        super.readAdditional(compound);
        this.dataManager.set(PLOWING, compound.getBoolean("Plowing"));
    }

    @Override
    protected void writeAdditional(final CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("Plowing", this.dataManager.get(PLOWING));

    }

    private void openContainer(final PlayerEntity player) {
        if (player instanceof ServerPlayerEntity) {
            NetworkHooks.openGui((ServerPlayerEntity) player,
                new SimpleNamedContainerProvider((windowId, playerInventory, p) -> new PlowCartContainer(windowId, playerInventory, this), this.getDisplayName()),
                buf -> buf.writeInt(this.getEntityId())
            );
        }
    }
}
