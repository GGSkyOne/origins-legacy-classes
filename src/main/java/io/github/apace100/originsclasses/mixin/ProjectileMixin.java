package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.power.ClassesPowerTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Projectile.class)
public class ProjectileMixin {
    @ModifyVariable(
        method = "shootFromRotation(Lnet/minecraft/world/entity/Entity;FFFFF)V",
        at = @At("HEAD"),
        ordinal = 4,
        argsOnly = true
    )
    private float modifyDivergence(float oldDivergence, Entity user) {
        if (ClassesPowerTypes.NO_PROJECTILE_DIVERGENCE.isActive(user)) {
            return 0F;
        }

        return oldDivergence;
    }
}
