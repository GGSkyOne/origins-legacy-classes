package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.component.ClassesComponents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.LingeringPotionItem;
import net.minecraft.item.PotionItem;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin({PotionItem.class, LingeringPotionItem.class})
public class PotionItemMixin {
    @Inject(method = "appendTooltip", at = @At("HEAD"))
    @Environment(EnvType.CLIENT)
    private void appendExtendedTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type, CallbackInfo ci) {
        if (Boolean.TRUE.equals(stack.get(ClassesComponents.EXTENDED_BY_CLERIC))) {
            tooltip.add(Text.translatable("origins-classes.longer_potions").formatted(Formatting.GOLD));
        }
    }
}
