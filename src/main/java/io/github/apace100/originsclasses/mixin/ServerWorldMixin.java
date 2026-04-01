package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.effect.ClassesEffects;
import io.github.apace100.originsclasses.networking.ClassesPackets.BlockBreakParticlesPayload;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {
    @Shadow public abstract ServerWorld toServerWorld();

    @Inject(
        method = "syncWorldEvent",
        at = @At("HEAD"),
        cancellable = true
    )
    private void suppressStealthBlockBreakSound(PlayerEntity player, int eventId, BlockPos pos, int data, CallbackInfo ci) {
        if (eventId == 2001 && player != null && player.hasStatusEffect(ClassesEffects.STEALTH)) {
            ci.cancel();

            BlockBreakParticlesPayload payload = new BlockBreakParticlesPayload(pos, data);

            for (ServerPlayerEntity nearbyPlayer : PlayerLookup.tracking(this.toServerWorld(), pos)) {
                if (nearbyPlayer != player) {
                    ServerPlayNetworking.send(nearbyPlayer, payload);
                }
            }
        }
    }
}
