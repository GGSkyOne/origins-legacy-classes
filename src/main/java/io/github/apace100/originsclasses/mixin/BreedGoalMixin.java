package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.power.ClassesPowerTypes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BreedGoal.class)
public class BreedGoalMixin {
    @Shadow @Final protected Animal animal;
    @Shadow protected Animal partner;

    @Inject(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/ai/goal/BreedGoal;breed()V"
        )
    )
    private void produceAdditionalBaby(CallbackInfo ci) {
        Player lovingPlayer = this.animal.getLoveCause();

        if (lovingPlayer != null && ClassesPowerTypes.TWIN_BREEDING.isActive(lovingPlayer)) {
            if (this.animal.level().getRandom().nextInt(5) == 0) {
                animal.spawnChildFromBreeding((ServerLevel) animal.level(), this.partner);
            }
        }
    }
}
