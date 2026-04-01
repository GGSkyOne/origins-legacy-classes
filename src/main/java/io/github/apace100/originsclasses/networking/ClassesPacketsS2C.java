package io.github.apace100.originsclasses.networking;

import io.github.apace100.originsclasses.networking.ClassesPackets.BlockBreakParticlesPayload;
import io.github.apace100.originsclasses.networking.ClassesPackets.MultiMiningPayload;
import io.github.apace100.originsclasses.networking.ClassesPackets.TraderTypePayload;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;

public class ClassesPacketsS2C {
    private static boolean isWanderingTrader;
    private static boolean isMultiMining;

    public static boolean isWanderingTrader() {
        return isWanderingTrader;
    }

    public static boolean isMultiMining() {
        return isMultiMining;
    }

    @Environment(EnvType.CLIENT)
    public static void register() {
        ClientPlayConnectionEvents.INIT.register((handler, client) -> {
            ClientPlayNetworking.registerReceiver(TraderTypePayload.ID, (payload, context) ->
                context.client().execute(() -> isWanderingTrader = payload.isWanderingTrader()));

            ClientPlayNetworking.registerReceiver(MultiMiningPayload.ID, (payload, context) ->
                context.client().execute(() -> isMultiMining = payload.isMultiMining()));

            ClientPlayNetworking.registerReceiver(BlockBreakParticlesPayload.ID, (payload, context) ->
                context.client().execute(() -> {
                    if (context.client().level != null) {
                        BlockState state = Block.stateById(payload.rawStateId());
                        Objects.requireNonNull(context.client().level).addDestroyBlockEffect(payload.pos(), state);
                    }
                }));
        });
    }
}
