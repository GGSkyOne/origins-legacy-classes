package io.github.apace100.originsclasses.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.originsclasses.ducks.BlockBreakSneakState;
import io.github.apace100.originsclasses.networking.ClassesPacketsS2C;
import io.github.apace100.originsclasses.power.MultiMinePower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public class BlockBehaviourMixin {
    @Inject(
        method = "getDestroyProgress",
        at = @At("RETURN"),
        cancellable = true
    )
    private void modifyMultiMinedBlockBreakingDelta(BlockState state, Player player, BlockGetter world, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        boolean processMultimine = false;

        if (player instanceof ServerPlayer) {
            BlockBreakSneakState sneakingState = (BlockBreakSneakState)((ServerPlayer)player).gameMode;
            processMultimine = !sneakingState.wasSneakingWhenBlockBreakingStarted();
        } else {
            processMultimine = ClassesPacketsS2C.isMultiMining();
        }

        if (processMultimine) {
            ItemStack tool = player.getItemBySlot(EquipmentSlot.MAINHAND);

            int toolDurability = 128;

            if (!tool.isEmpty()) {
                toolDurability = tool.getMaxDamage() - tool.getDamageValue();
            }

            int finalToolDurability = toolDurability;

            PowerHolderComponent.KEY.get(player).getPowers(MultiMinePower.class).forEach(mmp -> {
                if (mmp.isBlockStateAffected(state)) {
                    int affectBlockCount = mmp.getAffectedBlocks(state, pos).size();

                    if (affectBlockCount > 0) {
                        int multiplier = Math.min(affectBlockCount, finalToolDurability - 1);
                        multiplier = (int)Math.ceil((float)multiplier * 0.75F);

                        cir.setReturnValue(cir.getReturnValueF() / multiplier);
                    }
                }
            });
        }
    }
}
