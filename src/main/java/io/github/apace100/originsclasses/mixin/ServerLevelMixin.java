package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.effect.ClassesEffects;
import io.github.apace100.originsclasses.networking.ClassesPackets.BlockBreakParticlesPayload;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {
    @Shadow public abstract ServerLevel getLevel();

    @Inject(
        method = "levelEvent",
        at = @At("HEAD"),
        cancellable = true
    )
    private void suppressStealthBlockBreakSound(Entity entity, int eventId, BlockPos pos, int data, CallbackInfo ci) {
        if (eventId == 2001 && entity instanceof LivingEntity livingEntity && livingEntity.hasEffect(ClassesEffects.STEALTH)) {
            ci.cancel();

            BlockBreakParticlesPayload payload = new BlockBreakParticlesPayload(pos, data);

            for (ServerPlayer nearbyPlayer : PlayerLookup.tracking(this.getLevel(), pos)) {
                if (nearbyPlayer != entity) {
                    ServerPlayNetworking.send(nearbyPlayer, payload);
                }
            }
        }
    }
}
