package de.mennomax.astikorcarts.common;

import java.util.function.Supplier;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.Lazy;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.common.capabilities.food.FoodCapability;

import de.mennomax.astikorcarts.common.items.AstikorItems;

import static de.mennomax.astikorcarts.AstikorCarts.ID;

public class AstikorItemGroup extends CreativeModeTab
{
    public static final CreativeModeTab CARTS = new AstikorItemGroup("astikorcarts", () -> new ItemStack(AstikorItems.WHEEL_TFC.get(Wood.OAK).get()));

    private final Lazy<ItemStack> iconStack;

    private AstikorItemGroup(String label, Supplier<ItemStack> iconSupplier)
    {
        super(ID + "." + label);
        this.iconStack = Lazy.of(() -> FoodCapability.setStackNonDecaying(iconSupplier.get()));
    }

    @Override
    public ItemStack makeIcon()
    {
        return iconStack.get();
    }
}