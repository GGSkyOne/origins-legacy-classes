package io.github.apace100.originsclasses.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.originsclasses.ducks.SneakingStateSavingManager;
import io.github.apace100.originsclasses.networking.ModPackets;
import io.github.apace100.originsclasses.power.MultiMinePower;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerInteractionManager.class)
public abstract class ServerPlayerInteractionManagerMixin implements SneakingStateSavingManager {
    @Shadow protected ServerWorld world;
    @Shadow @Final protected ServerPlayerEntity player;

    @Shadow public abstract void finishMining(BlockPos pos, int sequence, String reason);

    @Unique
    private BlockState justMinedBlockState;
    @Unique
    private boolean performingMultiMine = false;
    @Unique
    private boolean wasSneakingWhenStarted = false;

    @Inject(
        method = "processBlockBreakingAction",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/BlockState;onBlockBreakStart(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/PlayerEntity;)V",
            ordinal = 0
        )
    )
    private void saveSneakingState(BlockPos pos, PlayerActionC2SPacket.Action action, Direction direction, int worldHeight, int sequence, CallbackInfo ci) {
        wasSneakingWhenStarted = player.isSneaking();
        ServerPlayNetworking.send(player, new ModPackets.MultiMiningPayload(!wasSneakingWhenStarted));
    }

    @Inject(
        method = "finishMining",
        at = @At("HEAD")
    )
    private void saveBlockStateForMultiMine(BlockPos pos, int sequence, String reason, CallbackInfo ci) {
        justMinedBlockState = world.getBlockState(pos);
    }

    @Inject(
        method = "finishMining",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/network/ServerPlayerInteractionManager;onBlockBreakingAction(Lnet/minecraft/util/math/BlockPos;ZILjava/lang/String;)V",
            ordinal = 0
        )
    )
    private void multiMinePower(BlockPos pos, int sequence, String reason, CallbackInfo ci) {
        if (!wasSneakingWhenStarted && !performingMultiMine) {
            performingMultiMine = true;

            PowerHolderComponent.KEY.get(player).getPowers(MultiMinePower.class).forEach(mmp -> {
                if (mmp.isBlockStateAffected(justMinedBlockState)) {
                    ItemStack tool = player.getMainHandStack().copy();

                    for (BlockPos bp : mmp.getAffectedBlocks(justMinedBlockState, pos)) {
                        finishMining(bp, sequence, reason);

                        if (!ItemStack.areItemsEqual(player.getMainHandStack(), tool)) {
                            break;
                        }
                    }
                }
            });

            performingMultiMine = false;
        }
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    public boolean wasSneakingWhenBlockBreakingStarted() {
        return wasSneakingWhenStarted;
    }
}
