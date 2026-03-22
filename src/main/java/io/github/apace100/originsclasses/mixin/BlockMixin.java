package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.power.ClassesPowerTypes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.world.level.block.Block.dropResources;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(method = "playerDestroy", at = @At("TAIL"))
    private void dropAdditionalCrops(Level world, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack stack, CallbackInfo ci) {
        if (state.getBlock() instanceof CropBlock || state.is(Blocks.MELON)) {
            if (player != null && ClassesPowerTypes.MORE_CROP_DROPS.isActive(player) && world.getRandom().nextInt(10) < 3) {
                dropResources(state, world, pos, blockEntity, player, stack);
            }
        }
    }

    @ModifyConstant(
        method = "playerDestroy",
        constant = @Constant(
            floatValue = 0.005F
        )
    )
    private float preventBlockMiningExhaustion(float exhaustion, Level world, Player playerEntity) {
        if (ClassesPowerTypes.NO_MINING_EXHAUSTION.isActive(playerEntity)) {
            return 0F;
        }

        return exhaustion;
    }
}
