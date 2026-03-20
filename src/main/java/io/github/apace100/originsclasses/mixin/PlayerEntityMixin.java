package io.github.apace100.originsclasses.mixin;

import io.github.apace100.apoli.power.VariableIntPower;
import io.github.apace100.originsclasses.effect.ClassesEffects;
import io.github.apace100.originsclasses.power.ClassPowerTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyVariable(
        method = "attack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerEntity;getAttackCooldownProgress(F)F"
        ),
        ordinal = 0
    )
    private float modifyBaseAttackDamageInStealth(float originalAttackDamage, Entity target) {
        float modifiedDamage = originalAttackDamage;
        boolean isInStealth = this.hasStatusEffect(ClassesEffects.STEALTH);

        if (target != null && isInStealth) {
            float yawTarget = target.getYaw(1F);

            while (yawTarget < 0F)
                yawTarget += 360F;

            yawTarget %= 360F;
            float yawSelf = this.getYaw(1F);

            while (yawSelf < 0F)
                yawSelf += 360F;

            yawSelf %= 360F;
            float yawDiff = Math.abs(yawTarget - yawSelf);

            if (yawDiff < 80) {
                modifiedDamage *= 2F;
            }
        }

        if (ClassPowerTypes.STEALTH.isActive(this)) {
            VariableIntPower stealthCounter = ClassPowerTypes.STEALTH.get(this);
            stealthCounter.setValue(stealthCounter.getMin());
        }

        if (isInStealth) {
            this.removeStatusEffect(ClassesEffects.STEALTH);
        }

        return modifiedDamage;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tickStealthCounter(CallbackInfo ci) {
        if (ClassPowerTypes.STEALTH.isActive(this)) {
            VariableIntPower stealthCounter = ClassPowerTypes.STEALTH.get(this);

            if (this.isSneaking()) {
                if (stealthCounter.increment() == stealthCounter.getMax()) {
                    if (!this.hasStatusEffect(ClassesEffects.STEALTH)) {
                        this.addStatusEffect(new StatusEffectInstance(ClassesEffects.STEALTH, 33000, 0, false, false, true));
                    }
                }
            } else {
                stealthCounter.setValue(stealthCounter.getMin());

                if (this.hasStatusEffect(ClassesEffects.STEALTH)) {
                    this.removeStatusEffect(ClassesEffects.STEALTH);
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
        if (this.hasStatusEffect(ClassesEffects.STEALTH)) {
            ci.cancel();
        }
    }

    @Redirect(
        method = "eatFood",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"
        )
    )
    private void muffleEatingFinishSound(World world, PlayerEntity player, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch) {
        if (!this.hasStatusEffect(ClassesEffects.STEALTH)) {
            world.playSound(player, x, y, z, sound, category, volume, pitch);
        }
    }
}
