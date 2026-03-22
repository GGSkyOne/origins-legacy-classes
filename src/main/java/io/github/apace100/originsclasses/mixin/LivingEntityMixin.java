package io.github.apace100.originsclasses.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.apace100.originsclasses.power.ClassesPowerTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @SuppressWarnings({"ConstantValue", "rawtypes"})
    @Inject(
        method = "dropFromLootTable(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;ZLnet/minecraft/resources/ResourceKey;Ljava/util/function/Consumer;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/storage/loot/LootTable;getRandomItems(Lnet/minecraft/world/level/storage/loot/LootParams;JLjava/util/function/Consumer;)V"
        )
    )
    private void dropAdditionalRancherLoot(ServerLevel serverLevel, DamageSource damageSource, boolean bl, ResourceKey resourceKey, Consumer<ItemStack> consumer, CallbackInfo ci, @Local LootTable lootTable, @Local LootParams lootContextParameterSet) {
        if (
            bl &&
            (Object)this instanceof Animal &&
            ClassesPowerTypes.MORE_ANIMAL_LOOT.isActive(damageSource.getEntity())
        ) {
            if (this.getRandom().nextInt(10) < 3) {
                lootTable
                    .getRandomItems(lootContextParameterSet)
                    .forEach(stack -> this.spawnAtLocation(serverLevel, stack));
            }
        }
    }

    @Inject(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;)Z", at = @At("RETURN"))
    private void addEffect(MobEffectInstance effect, CallbackInfoReturnable<Boolean> ci) {
        if (ci.getReturnValue() && !effect.isAmbient()) {
            if (ClassesPowerTypes.TAMED_POTION_DIFFUSAL.isActive(this)) {
                level().getEntitiesOfClass(
                    TamableAnimal.class,
                    getBoundingBox()
                        .expandTowards(8F, 2F, 8F)
                        .expandTowards(-8f, -2F, -8F),
                    e -> e.getOwner() == (Object) this
                )
                .forEach(e -> e.addEffect(effect));
            }
        }
    }
}
