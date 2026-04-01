package io.github.apace100.originsclasses.networking;

import io.github.apace100.originsclasses.OriginsClasses;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ClassesPackets {
    public record TraderTypePayload(boolean isWanderingTrader) implements CustomPayload {
        public static final Id<TraderTypePayload> ID = new Id<>(Identifier.of(OriginsClasses.MODID, "trader_type"));
        public static final PacketCodec<PacketByteBuf, TraderTypePayload> CODEC = PacketCodecs.BOOL.xmap(TraderTypePayload::new, TraderTypePayload::isWanderingTrader).cast();

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    public record MultiMiningPayload(boolean isMultiMining) implements CustomPayload {
        public static final Id<MultiMiningPayload> ID = new Id<>(Identifier.of(OriginsClasses.MODID, "multi_mining"));
        public static final PacketCodec<PacketByteBuf, MultiMiningPayload> CODEC = PacketCodecs.BOOL.xmap(MultiMiningPayload::new, MultiMiningPayload::isMultiMining).cast();

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    public record BlockBreakParticlesPayload(BlockPos pos, int rawStateId) implements CustomPayload {
        public static final Id<BlockBreakParticlesPayload> ID = new Id<>(Identifier.of(OriginsClasses.MODID, "block_break_particles"));
        public static final PacketCodec<PacketByteBuf, BlockBreakParticlesPayload> CODEC = PacketCodec.of(
            (value, buf) -> { buf.writeBlockPos(value.pos()); buf.writeInt(value.rawStateId()); },
            buf -> new BlockBreakParticlesPayload(buf.readBlockPos(), buf.readInt())
        );

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    public static void registerPayloads() {
        PayloadTypeRegistry.playS2C().register(TraderTypePayload.ID, TraderTypePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(MultiMiningPayload.ID, MultiMiningPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(BlockBreakParticlesPayload.ID, BlockBreakParticlesPayload.CODEC);
    }
}
