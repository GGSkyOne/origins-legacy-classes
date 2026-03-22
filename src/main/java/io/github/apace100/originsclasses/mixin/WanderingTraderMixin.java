package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.networking.ClassesPackets;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WanderingTrader.class)
public class WanderingTraderMixin {
    @Inject(
        method = "mobInteract",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/npc/wanderingtrader/WanderingTrader;getOffers()Lnet/minecraft/world/item/trading/MerchantOffers;",
            shift = At.Shift.AFTER
        )
    )
    private void sendTraderType(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (player.level().isClientSide()) {
            return;
        }

        ServerPlayNetworking.send((ServerPlayer) player, new ClassesPackets.TraderTypePayload(true));
    }
}
