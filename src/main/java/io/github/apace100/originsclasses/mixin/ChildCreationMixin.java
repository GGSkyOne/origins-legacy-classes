package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.power.ClassesPowerTypes;
import io.github.apace100.originsclasses.util.EntityUtil;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.animal.equine.Horse;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class ChildCreationMixin {
    @Mixin(Wolf.class)
    public static abstract class WolfKids extends TamableAnimal {
        protected WolfKids(EntityType<? extends TamableAnimal> entityType, Level world) {
            super(entityType, world);
        }

        @Inject(method = "getBreedOffspring*", at = @At("RETURN"))
        private void applyBeastmasterBoost(ServerLevel serverWorld, AgeableMob passiveEntity, CallbackInfoReturnable<Wolf> cir) {
            if (ClassesPowerTypes.TAMED_ANIMAL_BOOST.isActive(this.getOwner())) {
                EntityUtil.addBeastmasterAttributes(cir.getReturnValue());
            }
        }
    }

    @Mixin(Cat.class)
    public static abstract class CatKids extends TamableAnimal {
        protected CatKids(EntityType<? extends TamableAnimal> entityType, Level world) {
            super(entityType, world);
        }

        @Inject(method = "getBreedOffspring*", at = @At("RETURN"))
        private void applyBeastmasterBoost(ServerLevel serverWorld, AgeableMob passiveEntity, CallbackInfoReturnable<Cat> cir) {
            if (ClassesPowerTypes.TAMED_ANIMAL_BOOST.isActive(this.getOwner())) {
                EntityUtil.addBeastmasterAttributes(cir.getReturnValue());
            }
        }
    }

    @Mixin(Horse.class)
    public static abstract class HorseKids extends AbstractHorse {
        protected HorseKids(EntityType<? extends AbstractHorse> entityType, Level world) {
            super(entityType, world);
        }

        @Inject(method = "getBreedOffspring", at = @At("RETURN"))
        private void applyBeastmasterBoost(ServerLevel serverWorld, AgeableMob passiveEntity, CallbackInfoReturnable<Horse> cir) {
            if (ClassesPowerTypes.TAMED_ANIMAL_BOOST.isActive(this.getOwner())) {
                EntityUtil.addBeastmasterAttributes(cir.getReturnValue());
            }
        }
    }

}
