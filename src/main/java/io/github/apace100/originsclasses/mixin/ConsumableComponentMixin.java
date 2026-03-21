package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.component.ClassesComponents;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ConsumableComponent.class)
public class ConsumableComponentMixin {
    @Inject(method = "finishConsumption", at = @At("HEAD"))
    private void addFoodBonus(World world, LivingEntity user, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        if (!(user instanceof PlayerEntity player)) return;

        Integer foodBonus = stack.get(ClassesComponents.FOOD_BONUS);

        if (foodBonus != null) {
            player.getHungerManager().add(foodBonus, foodBonus * 0.2F);
        }
    }
}
