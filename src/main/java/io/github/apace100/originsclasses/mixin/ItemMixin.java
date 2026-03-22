package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.component.ClassesComponents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Inject(method = "appendHoverText", at = @At("HEAD"))
    @Environment(EnvType.CLIENT)
    private void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag type, CallbackInfo ci) {
        Integer foodBonus = stack.get(ClassesComponents.FOOD_BONUS);

        if (foodBonus != null) {
            textConsumer.accept(Component.translatable("origins-classes.food_bonus", foodBonus).withStyle(ChatFormatting.GRAY));
        }

        Float multiplier = stack.get(ClassesComponents.MINING_SPEED_MULTIPLIER);

        if (multiplier != null) {
            int bonusInt = Math.round((multiplier - 1F) * 100);

            String bonus = bonusInt > 0 ? ("+" + bonusInt + "%") : (bonusInt + "%");
            textConsumer.accept(Component.translatable("origins-classes.mining_speed_bonus", bonus).withStyle(ChatFormatting.BLUE));
        }

        if (Boolean.TRUE.equals(stack.get(ClassesComponents.EXTENDED_BY_CLERIC))) {
            textConsumer.accept(Component.translatable("origins-classes.longer_potions").withStyle(ChatFormatting.GOLD));
        }
    }

    @Inject(
        method = "getDestroySpeed",
        at = @At("RETURN"),
        cancellable = true
    )
    private void applyDestroySpeedMultiplier(ItemStack stack, BlockState state, CallbackInfoReturnable<Float> cir) {
        if (stack != null) {
            Float multiplier = stack.get(ClassesComponents.MINING_SPEED_MULTIPLIER);

            if (multiplier != null) {
                float base = cir.getReturnValueF();

                if (base > 1.0F) {
                    cir.setReturnValue(base * multiplier);
                }
            }
        }
    }
}
