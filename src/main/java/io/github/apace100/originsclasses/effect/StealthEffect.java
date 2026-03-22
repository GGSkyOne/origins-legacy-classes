package io.github.apace100.originsclasses.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class StealthEffect extends MobEffect {
    public static final MobEffect INSTANCE = new StealthEffect(MobEffectCategory.BENEFICIAL, 0x242424);

    protected StealthEffect(MobEffectCategory type, int color) {
        super(type, color);
    }
}
