package io.github.apace100.originsclasses.mixin;

import io.github.apace100.apoli.power.VariableIntPower;
import io.github.apace100.originsclasses.effect.ClassesEffects;
import io.github.apace100.originsclasses.power.ClassesPowerTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @ModifyVariable(
        method = "attack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;getAttackStrengthScale(F)F"
        ),
        ordinal = 0
    )
    private float modifyBaseAttackDamageInStealth(float originalAttackDamage, Entity target) {
        float modifiedDamage = originalAttackDamage;
        boolean isInStealth = this.hasEffect(ClassesEffects.STEALTH);

        if (target != null && isInStealth) {
            float yawTarget = target.getViewYRot(1F);

            while (yawTarget < 0F)
                yawTarget += 360F;

            yawTarget %= 360F;
            float yawSelf = this.getViewYRot(1F);

            while (yawSelf < 0F)
                yawSelf += 360F;

            yawSelf %= 360F;
            float yawDiff = Math.abs(yawTarget - yawSelf);

            if (yawDiff < 80) {
                modifiedDamage *= 2F;
            }
        }

        if (ClassesPowerTypes.STEALTH.isActive(this)) {
            VariableIntPower stealthCounter = ClassesPowerTypes.STEALTH.get(this);
            stealthCounter.setValue(stealthCounter.getMin());
        }

        if (isInStealth) {
            this.removeEffect(ClassesEffects.STEALTH);
        }

        return modifiedDamage;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tickStealthCounter(CallbackInfo ci) {
        if (ClassesPowerTypes.STEALTH.isActive(this)) {
            VariableIntPower stealthCounter = ClassesPowerTypes.STEALTH.get(this);

            if (this.isShiftKeyDown()) {
                if (stealthCounter.increment() == stealthCounter.getMax()) {
                    if (!this.hasEffect(ClassesEffects.STEALTH)) {
                        this.addEffect(new MobEffectInstance(ClassesEffects.STEALTH, 33000, 0, false, false, true));
                    }
                }
            } else {
                stealthCounter.setValue(stealthCounter.getMin());

                if (this.hasEffect(ClassesEffects.STEALTH)) {
                    this.removeEffect(ClassesEffects.STEALTH);
                }
            }
        }
    }

    @Inject(
        method = "playSound",
        at = @At("HEAD"),
        cancellable = true
    )
    private void muffleSoundsInStealth(SoundEvent sound, float volume, float pitch, CallbackInfo ci) {
        if (this.hasEffect(ClassesEffects.STEALTH)) {
            ci.cancel();
        }
    }
}
