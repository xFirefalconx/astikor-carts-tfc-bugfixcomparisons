package de.mennomax.astikorcarts.util;

import javax.annotation.Nonnull;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import de.mennomax.astikorcarts.entity.AbstractDrawnEntity;
import de.mennomax.astikorcarts.entity.AbstractDrawnInventoryEntity;

public class CartItemStackHandler<T extends AbstractDrawnEntity> extends ItemStackHandler
{
    protected final T cart;

    public CartItemStackHandler(final int slots, final T cart)
    {
        super(slots);
        this.cart = cart;
    }

    @Override
    public void deserializeNBT(final CompoundTag nbt)
    {
        nbt.remove("Size");
        super.deserializeNBT(nbt);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack)
    {
        return AbstractDrawnInventoryEntity.isValid(stack);
    }
}
