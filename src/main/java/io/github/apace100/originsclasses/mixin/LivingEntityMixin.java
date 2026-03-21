package io.github.apace100.originsclasses.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.apace100.originsclasses.power.ClassesPowerTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @SuppressWarnings("ConstantValue")
    @Inject(
        method = "generateLoot",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/loot/LootTable;generateLoot(Lnet/minecraft/loot/context/LootWorldContext;JLjava/util/function/Consumer;)V"
        )
    )
    private void dropAdditionalRancherLoot(ServerWorld world, DamageSource damageSource, boolean causedByPlayer, RegistryKey<LootTable> lootTableKey, Consumer<ItemStack> lootConsumer, CallbackInfo ci, @Local LootTable lootTable, @Local LootWorldContext lootContextParameterSet) {
        if (
            causedByPlayer &&
            (Object)this instanceof AnimalEntity &&
            ClassesPowerTypes.MORE_ANIMAL_LOOT.isActive(damageSource.getAttacker())
        ) {
            if (this.getRandom().nextInt(10) < 3) {
                lootTable
                    .generateLoot(lootContextParameterSet)
                    .forEach(stack -> this.dropStack(world, stack));
            }
        }
    }

    @Inject(method = "addStatusEffect*", at = @At("RETURN"))
    private void addStatusEffect(StatusEffectInstance effect, CallbackInfoReturnable<Boolean> ci) {
        if (ci.getReturnValue() && !effect.isAmbient()) {
            if (ClassesPowerTypes.TAMED_POTION_DIFFUSAL.isActive(this)) {
                getEntityWorld().getEntitiesByClass(
                    TameableEntity.class,
                    getBoundingBox()
                        .stretch(8F, 2F, 8F)
                        .stretch(-8f, -2F, -8F),
                    e -> e.getOwner() == (Object) this
                )
                .forEach(e -> e.addStatusEffect(effect));
            }
        }
    }
}
