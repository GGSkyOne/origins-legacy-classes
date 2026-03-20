package io.github.apace100.originsclasses.effect;

import io.github.apace100.originsclasses.OriginsClasses;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public final class ClassesEffects {
    public static RegistryEntry<StatusEffect> STEALTH;

    public static void register() {
        Registry.register(Registries.STATUS_EFFECT, Identifier.of(OriginsClasses.MODID, "stealth"), StealthEffect.INSTANCE);
        STEALTH = Registries.STATUS_EFFECT.getEntry(StealthEffect.INSTANCE);
    }
}
