package io.github.apace100.originsclasses.component;

import com.mojang.serialization.Codec;
import io.github.apace100.originsclasses.OriginsClasses;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.UnaryOperator;

public final class ClassesComponents {
    public static final ComponentType<Boolean> EXTENDED_BY_CLERIC = register(
        "extended_by_cleric", builder -> builder.codec(Codec.BOOL).packetCodec(PacketCodecs.BOOL)
    );

    public static final ComponentType<Integer> FOOD_BONUS = register(
        "food_bonus", builder -> builder.codec(Codec.INT).packetCodec(PacketCodecs.INTEGER)
    );

    public static final ComponentType<Float> MINING_SPEED_MULTIPLIER = register(
        "mining_speed_multiplier", builder -> builder.codec(Codec.FLOAT).packetCodec(PacketCodecs.FLOAT)
    );

    private static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(OriginsClasses.MODID, id), builderOperator.apply(ComponentType.<T>builder()).build());
    }

    public static void register() {}
}
