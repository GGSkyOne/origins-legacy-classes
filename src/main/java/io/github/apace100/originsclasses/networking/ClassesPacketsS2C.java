package io.github.apace100.originsclasses.networking;

import io.github.apace100.originsclasses.networking.ClassesPackets.MultiMiningPayload;
import io.github.apace100.originsclasses.networking.ClassesPackets.TraderTypePayload;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

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
        });
    }
}
