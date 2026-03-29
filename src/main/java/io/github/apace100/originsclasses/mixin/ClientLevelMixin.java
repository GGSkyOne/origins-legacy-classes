package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.effect.ClassesEffects;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {
    @Inject(
        method = "levelEvent",
        at = @At("HEAD"),
        cancellable = true
    )
    private void cancelEffectsDuringStealth(Entity entity, int eventId, BlockPos pos, int data, CallbackInfo ci) {
        if (!(entity instanceof LivingEntity livingEntity) || !livingEntity.hasEffect(ClassesEffects.STEALTH)) return;

        if (eventId == 2001) {
            ci.cancel();
            BlockState state = Block.stateById(data);

            if (!state.isAir()) {
                ((ClientLevel) (Object) this).addDestroyBlockEffect(pos, state);
            }
        }
    }
}
