package io.github.apace100.originsclasses.mixin;

import com.mojang.authlib.GameProfile;
import io.github.apace100.originsclasses.power.ClassesPowerTypes;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec2f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Shadow public abstract Hand getActiveHand();

    @Inject(
        method = "applyMovementSpeedFactors",
        at = @At("RETURN"),
        cancellable = true
    )
    private void modifyItemUseSlowdown(Vec2f input, CallbackInfoReturnable<Vec2f> cir) {
        ItemStack stackInUse = this.getStackInHand(this.getActiveHand());

        if (stackInUse.getItem() instanceof ShieldItem && ClassesPowerTypes.LESS_SHIELD_SLOWDOWN.isActive(this)) {
            Vec2f r = cir.getReturnValue();
            cir.setReturnValue(new Vec2f(r.x * 3.0f, r.y * 3.0f));
        } else if (stackInUse.getItem() instanceof BowItem && ClassesPowerTypes.LESS_BOW_SLOWDOWN.isActive(this)) {
            Vec2f r = cir.getReturnValue();
            cir.setReturnValue(new Vec2f(r.x * 3.0f, r.y * 3.0f));
        }
    }
}
