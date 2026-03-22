package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.util.EnchantmentContext;
import net.minecraft.world.item.enchantment.Enchantable;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @Redirect(
        method = "selectEnchantment",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/enchantment/Enchantable;value()I"
        )
    )
    private static int modifyEnchantabilityForClerics(Enchantable component) {
        int base = component.value();

        if (base > 0 && EnchantmentContext.isClericEnchanting()) {
            return base + 10;
        }

        return base;
    }

    @Inject(
        method = "selectEnchantment",
        at = @At("TAIL")
    )
    private static void resetClericContext(CallbackInfoReturnable<?> cir) {
        EnchantmentContext.setClericEnchanting(false);
    }
}
