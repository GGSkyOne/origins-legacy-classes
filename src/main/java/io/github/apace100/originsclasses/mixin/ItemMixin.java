package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.component.ClassesComponents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.Item;
import net.minecraft.item.Item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Inject(method = "appendTooltip", at = @At("HEAD"))
    @Environment(EnvType.CLIENT)
    private void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type, CallbackInfo ci) {
        Integer foodBonus = stack.get(ClassesComponents.FOOD_BONUS);

        if (foodBonus != null) {
            textConsumer.accept(Text.translatable("origins-classes.food_bonus", foodBonus).formatted(Formatting.GRAY));
        }

        Float multiplier = stack.get(ClassesComponents.MINING_SPEED_MULTIPLIER);

        if (multiplier != null) {
            int bonusInt = Math.round((multiplier - 1F) * 100);

            String bonus = bonusInt > 0 ? ("+" + bonusInt + "%") : (bonusInt + "%");
            textConsumer.accept(Text.translatable("origins-classes.mining_speed_bonus", bonus).formatted(Formatting.BLUE));
        }

        if (Boolean.TRUE.equals(stack.get(ClassesComponents.EXTENDED_BY_CLERIC))) {
            textConsumer.accept(Text.translatable("origins-classes.longer_potions").formatted(Formatting.GOLD));
        }
    }
}
