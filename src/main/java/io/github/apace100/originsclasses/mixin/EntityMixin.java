package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.effect.ClassesEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "occludeVibrationSignals", at = @At("HEAD"), cancellable = true)
    private void suppressSculkInStealth(CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof LivingEntity entity) {
            cir.setReturnValue(entity.hasStatusEffect(ClassesEffects.STEALTH));
        }
    }
}