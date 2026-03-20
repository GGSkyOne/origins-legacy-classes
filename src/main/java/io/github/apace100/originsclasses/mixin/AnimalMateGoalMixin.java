package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.power.ClassesPowerTypes;
import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnimalMateGoal.class)
public class AnimalMateGoalMixin {
    @Shadow @Final protected AnimalEntity animal;
    @Shadow protected AnimalEntity mate;

    @Inject(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/ai/goal/AnimalMateGoal;breed()V"
        )
    )
    private void produceAdditionalBaby(CallbackInfo ci) {
        PlayerEntity lovingPlayer = this.animal.getLovingPlayer();

        if (lovingPlayer != null && ClassesPowerTypes.TWIN_BREEDING.isActive(lovingPlayer)) {
            if (this.animal.getEntityWorld().getRandom().nextInt(5) == 0) {
                animal.breed((ServerWorld) animal.getEntityWorld(), this.mate);
            }
        }
    }
}
