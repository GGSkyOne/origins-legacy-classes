package io.github.apace100.originsclasses.component;

import com.mojang.serialization.Codec;
import io.github.apace100.originsclasses.OriginsClasses;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

import java.util.function.UnaryOperator;

public final class ClassesComponents {
    public static final DataComponentType<Boolean> EXTENDED_BY_CLERIC = register(
        "extended_by_cleric", builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL)
    );

    public static final DataComponentType<Integer> FOOD_BONUS = register(
        "food_bonus", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT)
    );

    public static final DataComponentType<Float> MINING_SPEED_MULTIPLIER = register(
        "mining_speed_multiplier", builder -> builder.persistent(Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT)
    );

    private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Identifier.fromNamespaceAndPath(OriginsClasses.MODID, id), builderOperator.apply(DataComponentType.<T>builder()).build());
    }

    public static void register() {}
}
