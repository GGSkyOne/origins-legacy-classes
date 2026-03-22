package io.github.apace100.originsclasses.mixin;

import com.mojang.authlib.GameProfile;
import io.github.apace100.originsclasses.power.ClassesPowerTypes;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.phys.Vec2;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer {
    public LocalPlayerMixin(ClientLevel world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(
        method = "modifyInput",
        at = @At("RETURN"),
        cancellable = true
    )
    private void modifyItemUseSlowdown(Vec2 input, CallbackInfoReturnable<Vec2> cir) {
        ItemStack stackInUse = this.getItemInHand(this.getUsedItemHand());

        if (stackInUse.getItem() instanceof ShieldItem && ClassesPowerTypes.LESS_SHIELD_SLOWDOWN.isActive(this)) {
            Vec2 r = cir.getReturnValue();
            cir.setReturnValue(new Vec2(r.x * 3.0f, r.y * 3.0f));
        } else if (stackInUse.getItem() instanceof BowItem && ClassesPowerTypes.LESS_BOW_SLOWDOWN.isActive(this)) {
            Vec2 r = cir.getReturnValue();
            cir.setReturnValue(new Vec2(r.x * 3.0f, r.y * 3.0f));
        }
    }
}
