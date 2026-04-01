package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.effect.ClassesEffects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin {
    @Shadow public abstract void addBlockBreakParticles(BlockPos pos, BlockState state);

    @Inject(
        method = "syncWorldEvent",
        at = @At("HEAD"),
        cancellable = true
    )
    private void cancelEffectsDuringStealth(PlayerEntity player, int eventId, BlockPos pos, int data, CallbackInfo ci) {
        if (eventId == 2001 && player != null && player.hasStatusEffect(ClassesEffects.STEALTH)) {
            ci.cancel();

            BlockState state = Block.getStateFromRawId(data);
            this.addBlockBreakParticles(pos, state);
        }
    }
}
