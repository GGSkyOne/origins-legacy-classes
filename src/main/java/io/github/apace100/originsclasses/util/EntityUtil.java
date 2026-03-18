package io.github.apace100.originsclasses.util;

import io.github.apace100.originsclasses.OriginsClasses;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.Identifier;

public final class EntityUtil {
    public static void addBeastmasterAttributes(LivingEntity entity) {
        if (entity.getAttributes().hasAttribute(EntityAttributes.GENERIC_MAX_HEALTH)) {
            entity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)
                .addPersistentModifier(
                    new EntityAttributeModifier(
                        Identifier.of(OriginsClasses.MODID,
                        "beastmaster_health"),
                        0.3,
                        EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                    )
                );
        }

        if (entity.getAttributes().hasAttribute(EntityAttributes.GENERIC_ATTACK_DAMAGE)) {
            entity.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE)
                .addPersistentModifier(
                    new EntityAttributeModifier(
                        Identifier.of(OriginsClasses.MODID,
                        "beastmaster_attack"),
                        1.5,
                        EntityAttributeModifier.Operation.ADD_VALUE
                    )
                );
        }
    }
}
