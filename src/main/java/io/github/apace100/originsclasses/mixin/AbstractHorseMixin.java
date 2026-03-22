package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.power.ClassesPowerTypes;
import io.github.apace100.originsclasses.util.EntityUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractHorse.class)
public class AbstractHorseMixin {
    @Inject(
        method = "tameWithName",
        at = @At("TAIL")
    )
    private void applyBeastmasterAttributes(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (ClassesPowerTypes.TAMED_ANIMAL_BOOST.isActive(player)) {
            EntityUtil.addBeastmasterAttributes((LivingEntity)(Object)this);
        }
    }
}
