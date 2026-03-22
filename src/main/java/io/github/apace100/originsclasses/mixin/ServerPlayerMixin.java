package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.power.ClassesPowerTypes;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
    @Redirect(
        method = "checkMovementStatistics",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerPlayer;causeFoodExhaustion(F)V",
            ordinal = 3
        )
    )
    private void removeSprintingExhaustion(ServerPlayer serverPlayerEntity, float exhaustion) {
        if (!ClassesPowerTypes.NO_SPRINT_EXHAUSTION.isActive(serverPlayerEntity)) {
            serverPlayerEntity.causeFoodExhaustion(exhaustion);
        }
    }
}
