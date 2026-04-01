package io.github.apace100.originsclasses.networking;

import io.github.apace100.originsclasses.OriginsClasses;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public class ClassesPackets {
    public record TraderTypePayload(boolean isWanderingTrader) implements CustomPacketPayload {
        public static final Type<TraderTypePayload> ID = new Type<>(Identifier.fromNamespaceAndPath(OriginsClasses.MODID, "trader_type"));
        public static final StreamCodec<FriendlyByteBuf, TraderTypePayload> CODEC = ByteBufCodecs.BOOL.map(TraderTypePayload::new, TraderTypePayload::isWanderingTrader).cast();

        @Override @NonNull
        public Type<? extends CustomPacketPayload> type() {
            return ID;
        }
    }

    public record MultiMiningPayload(boolean isMultiMining) implements CustomPacketPayload {
        public static final Type<MultiMiningPayload> ID = new Type<>(Identifier.fromNamespaceAndPath(OriginsClasses.MODID, "multi_mining"));
        public static final StreamCodec<FriendlyByteBuf, MultiMiningPayload> CODEC = ByteBufCodecs.BOOL.map(MultiMiningPayload::new, MultiMiningPayload::isMultiMining).cast();

        @Override @NonNull
        public Type<? extends CustomPacketPayload> type() {
            return ID;
        }
    }

    public record BlockBreakParticlesPayload(BlockPos pos, int rawStateId) implements CustomPacketPayload {
        public static final Type<BlockBreakParticlesPayload> ID = new Type<>(Identifier.fromNamespaceAndPath(OriginsClasses.MODID, "block_break_particles"));
        public static final StreamCodec<FriendlyByteBuf, BlockBreakParticlesPayload> CODEC = StreamCodec.of(
            (buf, value) -> { buf.writeBlockPos(value.pos()); buf.writeInt(value.rawStateId()); },
            buf -> new BlockBreakParticlesPayload(buf.readBlockPos(), buf.readInt())
        );

        @Override @NonNull
        public Type<? extends CustomPacketPayload> type() {
            return ID;
        }
    }

    public static void registerPayloads() {
        PayloadTypeRegistry.playS2C().register(TraderTypePayload.ID, TraderTypePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(MultiMiningPayload.ID, MultiMiningPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(BlockBreakParticlesPayload.ID, BlockBreakParticlesPayload.CODEC);
    }
}
