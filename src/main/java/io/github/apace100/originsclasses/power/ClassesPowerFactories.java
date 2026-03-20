package io.github.apace100.originsclasses.power;

import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.registry.Registry;

public class ClassesPowerFactories {
    public static void register() {
        register(CraftAmountPower.FACTORY);
        register(LumberjackPower.FACTORY);
        register(ClassesVariableIntPower.FACTORY);
    }

    private static void register(PowerFactory<?> factory) {
        Registry.register(ApoliRegistries.POWER_FACTORY, factory.getSerializerId(), factory);
    }
}
