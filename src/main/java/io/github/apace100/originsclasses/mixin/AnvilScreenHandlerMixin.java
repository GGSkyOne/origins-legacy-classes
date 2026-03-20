package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.power.ClassesPowerTypes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.ForgingSlotsManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler {
    @Shadow private static ForgingSlotsManager getForgingSlotsManager() {
        throw new AssertionError();
    }

    public AnvilScreenHandlerMixin(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context, getForgingSlotsManager());
    }

    @ModifyConstant(
        method = "updateResult",
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
        method = "updateResult",
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
