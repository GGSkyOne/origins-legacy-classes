package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.networking.ClassesPackets;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Villager.class)
public class VillagerMixin {
    @Inject(
        method = "startTrading",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/npc/villager/Villager;openTradingScreen(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/network/chat/Component;I)V",
            shift = At.Shift.AFTER
        )
    )
    private void sendTraderType(Player customer, CallbackInfo ci) {
        if (customer.level().isClientSide()) {
            return;
        }

        ServerPlayNetworking.send((ServerPlayer) customer, new ClassesPackets.TraderTypePayload(false));
    }
}
