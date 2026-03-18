package io.github.apace100.originsclasses.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.entry.RegistryEntry;

public class StealthEffect extends StatusEffect {
    public static final StatusEffect INSTANCE = new StealthEffect(StatusEffectCategory.BENEFICIAL, 0x242424);
    public static RegistryEntry<StatusEffect> ENTRY;

    protected StealthEffect(StatusEffectCategory type, int color) {
        super(type, color);
    }

}
