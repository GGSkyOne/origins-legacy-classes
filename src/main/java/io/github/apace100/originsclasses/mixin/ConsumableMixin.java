package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.component.ClassesComponents;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Consumable.class)
public class ConsumableMixin {
    @Inject(method = "onConsume", at = @At("HEAD"))
    private void addFoodBonus(Level world, LivingEntity user, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        if (!(user instanceof Player player)) return;

        Integer foodBonus = stack.get(ClassesComponents.FOOD_BONUS);

        if (foodBonus != null) {
            player.getFoodData().eat(foodBonus, foodBonus * 0.2F);
        }
    }
}
