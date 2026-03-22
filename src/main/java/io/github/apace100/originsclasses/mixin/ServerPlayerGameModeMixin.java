package io.github.apace100.originsclasses.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.originsclasses.ducks.BlockBreakSneakState;
import io.github.apace100.originsclasses.networking.ClassesPackets;
import io.github.apace100.originsclasses.power.MultiMinePower;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerGameMode.class)
public abstract class ServerPlayerGameModeMixin implements BlockBreakSneakState {
    @Shadow protected ServerLevel level;
    @Shadow @Final protected ServerPlayer player;

    @Shadow public abstract void destroyAndAck(BlockPos pos, int sequence, String reason);

    @Unique
    private BlockState justMinedBlockState;
    @Unique
    private boolean performingMultiMine = false;
    @Unique
    private boolean wasSneakingWhenStarted = false;

    @Inject(
        method = "handleBlockBreakAction",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;attack(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;)V",
            ordinal = 0
        )
    )
    private void saveSneakingState(BlockPos pos, ServerboundPlayerActionPacket.Action action, Direction direction, int worldHeight, int sequence, CallbackInfo ci) {
        wasSneakingWhenStarted = player.isShiftKeyDown();
        ServerPlayNetworking.send(player, new ClassesPackets.MultiMiningPayload(!wasSneakingWhenStarted));
    }

    @Inject(method = "destroyAndAck", at = @At("HEAD"))
    private void saveBlockStateForMultiMine(BlockPos pos, int sequence, String reason, CallbackInfo ci) {
        justMinedBlockState = level.getBlockState(pos);
    }

    @Inject(
        method = "destroyAndAck",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerPlayerGameMode;debugLogging(Lnet/minecraft/core/BlockPos;ZILjava/lang/String;)V",
            ordinal = 0
        )
    )
    private void multiMinePower(BlockPos pos, int sequence, String reason, CallbackInfo ci) {
        if (!wasSneakingWhenStarted && !performingMultiMine) {
            performingMultiMine = true;

            PowerHolderComponent.KEY.get(player).getPowers(MultiMinePower.class).forEach(mmp -> {
                if (mmp.isBlockStateAffected(justMinedBlockState)) {
                    ItemStack tool = player.getMainHandItem().copy();

                    for (BlockPos bp : mmp.getAffectedBlocks(justMinedBlockState, pos)) {
                        destroyAndAck(bp, sequence, reason);

                        if (!ItemStack.isSameItem(player.getMainHandItem(), tool)) {
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
