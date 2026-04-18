package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.power.ClassesPowerTypes;
import io.github.apace100.originsclasses.util.CraftingContext;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RepairItemRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(RepairItemRecipe.class)
public class RepairItemRecipeMixin {
    @ModifyConstant(
        method = "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;)Lnet/minecraft/world/item/ItemStack;",
        constant = @Constant(
            intValue = 5,
            ordinal = 0
        )
    )
    private int doubleRepairDurabilityBonus(int original) {
        Player player = CraftingContext.getCraftingPlayer();

        if (player != null && ClassesPowerTypes.EFFICIENT_REPAIRS.isActive(player)) {
            return original * 3;
        }

        return original;
    }
}
