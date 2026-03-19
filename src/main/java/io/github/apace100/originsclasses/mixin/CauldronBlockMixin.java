package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.component.ClassesComponents;
import io.github.apace100.originsclasses.power.ClassPowerTypes;
import net.minecraft.block.*;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(AbstractCauldronBlock.class)
public abstract class CauldronBlockMixin {
    @Inject(
        method = "onUseWithItem",
        at = @At(value = "RETURN", ordinal = 0),
        cancellable = true
    )
    private void extendPotionDuration(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ItemActionResult> cir) {
        if (state.isOf(Blocks.WATER_CAULDRON) && ClassPowerTypes.LONGER_POTIONS.isActive(player)) {
            int level = state.get(LeveledCauldronBlock.LEVEL);

            if (stack.getItem() instanceof PotionItem && level > 0 && !Boolean.TRUE.equals(stack.get(ClassesComponents.EXTENDED_BY_CLERIC))) {
                PotionContentsComponent contents = stack.get(DataComponentTypes.POTION_CONTENTS);

                if (contents != null && contents.getEffects().iterator().hasNext()) {
                    stack.set(DataComponentTypes.CUSTOM_NAME, stack.getName());
                    stack.set(ClassesComponents.EXTENDED_BY_CLERIC, true);

                    List<StatusEffectInstance> baseEffects = new ArrayList<>();
                    contents.getEffects().forEach(baseEffects::add);

                    List<StatusEffectInstance> srcEffects = contents.customEffects().isEmpty() ? baseEffects : new ArrayList<>(contents.customEffects());

                    List<StatusEffectInstance> customPotion = srcEffects.stream()
                        .map(effect -> new StatusEffectInstance(
                            effect.getEffectType(),
                            effect.getDuration() * (effect.getEffectType().value().isInstant() ? 1 : 2),
                            effect.getAmplifier(),
                            effect.isAmbient(),
                            effect.shouldShowParticles(),
                            effect.shouldShowIcon()))
                        .toList();

                    int color = new PotionContentsComponent(Optional.empty(), Optional.empty(), customPotion).getColor();
                    stack.set(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(Optional.empty(), Optional.of(color), customPotion));

                    LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
                    world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);

                    cir.setReturnValue(ItemActionResult.SUCCESS);
                }
            }
        }
    }
}
