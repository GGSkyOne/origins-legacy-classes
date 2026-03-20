package io.github.apace100.originsclasses.util;

import io.github.apace100.originsclasses.OriginsClasses;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public final class EntityUtil {
    private static final double BEASTMASTER_HEALTH_BONUS = 0.3;
    private static final double BEASTMASTER_ATTACK_BONUS = 1.5;

    public static void addBeastmasterAttributes(LivingEntity entity) {
        applyModifier(
            entity,
            EntityAttributes.GENERIC_MAX_HEALTH,
            Identifier.of(OriginsClasses.MODID, "beastmaster_health"),
            BEASTMASTER_HEALTH_BONUS, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );

        applyModifier(
            entity,
            EntityAttributes.GENERIC_ATTACK_DAMAGE,
            Identifier.of(OriginsClasses.MODID, "beastmaster_attack"),
            BEASTMASTER_ATTACK_BONUS, EntityAttributeModifier.Operation.ADD_VALUE
        );
    }

    private static void applyModifier(LivingEntity entity, RegistryEntry<EntityAttribute> attribute, Identifier id, double amount, EntityAttributeModifier.Operation operation) {
        if (entity.getAttributes().hasAttribute(attribute)) {
            EntityAttributeInstance inst = entity.getAttributeInstance(attribute);

            if (inst != null) {
                inst.addPersistentModifier(new EntityAttributeModifier(id, amount, operation));
            }
        }
    }
}
