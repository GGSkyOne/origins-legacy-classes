package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.effect.ClassesEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {
    @Inject(
        method = "levelEvent",
        at = @At("HEAD"),
        cancellable = true
    )
    private void cancelSyncingStealthEvents(Entity source, int eventId, BlockPos pos, int data, CallbackInfo ci) {
        if (source instanceof LivingEntity entity && entity.hasEffect(ClassesEffects.STEALTH)) {
            ci.cancel();
        }
    }
}
