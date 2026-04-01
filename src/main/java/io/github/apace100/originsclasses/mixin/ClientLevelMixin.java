package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.effect.ClassesEffects;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin {
    @Shadow public abstract void addDestroyBlockEffect(BlockPos pos, BlockState state);

    @Inject(
        method = "levelEvent",
        at = @At("HEAD"),
        cancellable = true
    )
    private void cancelEffectsDuringStealth(Entity entity, int eventId, BlockPos pos, int data, CallbackInfo ci) {
        if (eventId == 2001 && entity instanceof LivingEntity livingEntity && livingEntity.hasEffect(ClassesEffects.STEALTH)) {
            ci.cancel();
            BlockState state = Block.stateById(data);
            this.addDestroyBlockEffect(pos, state);
        }
    }
}
