package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.util.EnchantmentContext;
import net.minecraft.component.type.EnchantableComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @Redirect(
        method = "generateEnchantments",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/component/type/EnchantableComponent;value()I"
        )
    )
    private static int modifyEnchantabilityForClerics(EnchantableComponent component) {
        int base = component.value();

        if (base > 0 && EnchantmentContext.isClericEnchanting()) {
            return base + 10;
        }

        return base;
    }

    @Inject(
        method = "generateEnchantments",
        at = @At("TAIL")
    )
    private static void resetClericContext(CallbackInfoReturnable<?> cir) {
        EnchantmentContext.setClericEnchanting(false);
    }
}
