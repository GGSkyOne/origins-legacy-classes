package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.power.ClassesPowerTypes;
import io.github.apace100.originsclasses.util.CraftingContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.recipe.RepairItemRecipe;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(RepairItemRecipe.class)
public class RepairItemRecipeMixin {
    @ModifyConstant(
        method = "craft(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;",
        constant = @Constant(
            intValue = 5,
            ordinal = 0
        )
    )
    private int doubleRepairDurabilityBonus(int original, CraftingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        PlayerEntity player = CraftingContext.getCraftingPlayer();

        if (player != null && ClassesPowerTypes.EFFICIENT_REPAIRS.isActive(player)) {
            return original * 3;
        }

        return original;
    }
}
