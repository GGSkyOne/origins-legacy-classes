package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.power.ClassesPowerTypes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenu {
    @Shadow private static ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        throw new AssertionError();
    }

    public AnvilMenuMixin(MenuType<?> type, int syncId, Inventory playerInventory, ContainerLevelAccess context) {
        super(type, syncId, playerInventory, context, createInputSlotDefinitions());
    }

    @ModifyConstant(
        method = "createResult",
        constant = @Constant(
            intValue = 4,
            ordinal = 0
        )
    )
    private int halfRepairMaterialCost(int original) {
        if (ClassesPowerTypes.EFFICIENT_REPAIRS.isActive(player)) {
            return original / 2;
        }

        return original;
    }

    @ModifyConstant(
        method = "createResult",
        constant = @Constant(
            intValue = 12,
            ordinal = 0
        )
    )
    private int doubleCombineRepairDurabilityBonus(int original) {
        if (ClassesPowerTypes.EFFICIENT_REPAIRS.isActive(player)) {
            return original * 12;
        }

        return original;
    }
}
