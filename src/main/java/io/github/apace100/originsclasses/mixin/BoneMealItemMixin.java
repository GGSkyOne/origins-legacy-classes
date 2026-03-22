package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.power.ClassesPowerTypes;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BoneMealItem.class)
public class BoneMealItemMixin {
    @Unique
    private static boolean isFarmer = false;

    @Inject(
        method = "useOn",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/BlockPos;relative(Lnet/minecraft/core/Direction;)Lnet/minecraft/core/BlockPos;",
            shift = At.Shift.AFTER
        )
    )
    private void saveFarmerForLater(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        if (context.getPlayer() != null && ClassesPowerTypes.BETTER_BONE_MEAL.isActive(context.getPlayer())) {
            isFarmer = true;
        }
    }

    @Inject(
        method = "useOn",
        at = @At("RETURN")
    )
    private void removeSavedFarmer(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        isFarmer = false;
    }

    @Inject(
        method = "growCrop",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V"
        )
    )
    private static void applyAdditionalFarmerBoneMeal(ItemStack stack, Level world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (isFarmer) {
            BlockState blockState = world.getBlockState(pos);
            BonemealableBlock fertilizable = (BonemealableBlock)blockState.getBlock();

            if (fertilizable.isBonemealSuccess(world, world.getRandom(), pos, blockState)) {
                fertilizable.performBonemeal((ServerLevel)world, world.getRandom(), pos, blockState);
            }
        }
    }
}
