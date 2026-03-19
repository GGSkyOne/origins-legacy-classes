package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.power.ClassPowerTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Redirect(
        method = "increaseTravelMotionStats",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/network/ServerPlayerEntity;addExhaustion(F)V",
            ordinal = 3
        )
    )
    private void removeSprintingExhaustion(ServerPlayerEntity serverPlayerEntity, float exhaustion) {
        if (!ClassPowerTypes.NO_SPRINT_EXHAUSTION.isActive(serverPlayerEntity)) {
            serverPlayerEntity.addExhaustion(exhaustion);
        }
    }
}
