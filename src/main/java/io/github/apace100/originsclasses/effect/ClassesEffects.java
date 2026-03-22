package io.github.apace100.originsclasses.effect;

import io.github.apace100.originsclasses.OriginsClasses;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;

public final class ClassesEffects {
    public static Holder<MobEffect> STEALTH;

    public static void register() {
        Registry.register(BuiltInRegistries.MOB_EFFECT, Identifier.fromNamespaceAndPath(OriginsClasses.MODID, "stealth"), StealthEffect.INSTANCE);
        STEALTH = BuiltInRegistries.MOB_EFFECT.wrapAsHolder(StealthEffect.INSTANCE);
    }
}
