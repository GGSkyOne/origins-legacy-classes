package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.power.ClassesPowerTypes;
import net.minecraft.block.*;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

import static net.minecraft.block.Block.dropStacks;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(method = "afterBreak", at = @At("TAIL"))
    private void dropAdditionalCrops(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack stack, CallbackInfo ci) {
        if (state.getBlock() instanceof CropBlock || state.isOf(Blocks.MELON)) {
            if (player != null && ClassesPowerTypes.MORE_CROP_DROPS.isActive(player) && new Random().nextInt(10) < 3) {
                dropStacks(state, world, pos, blockEntity, player, stack);
            }
        }
    }

    @ModifyConstant(
        method = "afterBreak",
        constant = @Constant(
            floatValue = 0.005F
        )
    )
    private float preventBlockMiningExhaustion(float exhaustion, World world, PlayerEntity playerEntity) {
        if (ClassesPowerTypes.NO_MINING_EXHAUSTION.isActive(playerEntity)) {
            return 0F;
        }

        return exhaustion;
    }
}
