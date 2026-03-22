package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.component.ClassesComponents;
import io.github.apace100.originsclasses.power.ClassesPowerTypes;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(AbstractCauldronBlock.class)
public abstract class AbstractCauldronBlockMixin {
    @Inject(
        method = "useItemOn",
        at = @At(value = "RETURN", ordinal = 0),
        cancellable = true
    )
    private void extendPotionDuration(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        if (state.is(Blocks.WATER_CAULDRON) && ClassesPowerTypes.LONGER_POTIONS.isActive(player)) {
            int level = state.getValue(LayeredCauldronBlock.LEVEL);

            if (stack.getItem() instanceof PotionItem && level > 0 && !Boolean.TRUE.equals(stack.get(ClassesComponents.EXTENDED_BY_CLERIC))) {
                PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);

                if (contents != null && contents.getAllEffects().iterator().hasNext()) {
                    stack.set(DataComponents.CUSTOM_NAME, stack.getHoverName());
                    stack.set(ClassesComponents.EXTENDED_BY_CLERIC, true);

                    List<MobEffectInstance> baseEffects = new ArrayList<>();
                    contents.getAllEffects().forEach(baseEffects::add);

                    List<MobEffectInstance> srcEffects = contents.customEffects().isEmpty() ? baseEffects : new ArrayList<>(contents.customEffects());

                    List<MobEffectInstance> customPotion = srcEffects.stream()
                        .map(effect -> new MobEffectInstance(
                            effect.getEffect(),
                            effect.getDuration() * (effect.getEffect().value().isInstantenous() ? 1 : 2),
                            effect.getAmplifier(),
                            effect.isAmbient(),
                            effect.isVisible(),
                            effect.showIcon()))
                        .toList();

                    int color = new PotionContents(Optional.empty(), Optional.empty(), customPotion, Optional.empty()).getColor();
                    stack.set(DataComponents.POTION_CONTENTS, new PotionContents(Optional.empty(), Optional.of(color), customPotion, Optional.empty()));

                    LayeredCauldronBlock.lowerFillLevel(state, world, pos);
                    world.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    world.gameEvent(null, GameEvent.FLUID_PICKUP, pos);

                    cir.setReturnValue(InteractionResult.SUCCESS);
                }
            }
        }
    }
}
