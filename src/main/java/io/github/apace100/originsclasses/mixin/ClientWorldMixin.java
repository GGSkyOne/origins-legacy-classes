package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.effect.ClassesEffects;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {
    @Inject(
        method = "syncWorldEvent",
        at = @At("HEAD"),
        cancellable = true
    )
    private void cancelEffectsDuringStealth(Entity source, int eventId, BlockPos pos, int data, CallbackInfo ci) {
        if (source instanceof LivingEntity entity && entity.hasStatusEffect(ClassesEffects.STEALTH)) {
            ci.cancel();
        }
    }
}
