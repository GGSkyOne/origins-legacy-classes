package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.networking.ClassesPacketsS2C;
import io.github.apace100.originsclasses.power.ClassesPowerTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.npc.ClientSideMerchant;
import net.minecraft.world.item.trading.MerchantOffer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientSideMerchant.class)
public class ClientSideMerchantMixin {
    @Shadow @Final private Player source;

    @Redirect(
        method = "notifyTrade",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/trading/MerchantOffer;increaseUses()V"
        )
    )
    private void preventUseClientSide(MerchantOffer tradeOffer) {
        if (ClassesPacketsS2C.isWanderingTrader() || !ClassesPowerTypes.TRADE_AVAILABILITY.isActive(source)) {
            tradeOffer.increaseUses();
        }
    }
}
