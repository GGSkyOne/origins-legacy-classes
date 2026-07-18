package io.github.apace100.originsclasses.util;

import io.github.apace100.originsclasses.OriginsClasses;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;

public final class EntityUtil {
    private static final double BEASTMASTER_HEALTH_BONUS = 0.3;
    private static final double BEASTMASTER_ATTACK_BONUS = 1.5;

    public static void addBeastmasterAttributes(LivingEntity entity) {
        boolean healthApplied = applyModifier(
            entity,
            Attributes.MAX_HEALTH,
            Identifier.fromNamespaceAndPath(OriginsClasses.MODID, "beastmaster_health"),
            BEASTMASTER_HEALTH_BONUS,
            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );

        applyModifier(
            entity,
            Attributes.ATTACK_DAMAGE,
            Identifier.fromNamespaceAndPath(OriginsClasses.MODID, "beastmaster_attack"),
            BEASTMASTER_ATTACK_BONUS,
            AttributeModifier.Operation.ADD_VALUE
        );

        if (healthApplied) {
            entity.setHealth(entity.getMaxHealth());
        }
    }

    private static boolean applyModifier(LivingEntity entity, Holder<Attribute> attribute, Identifier id, double amount, AttributeModifier.Operation operation) {
        if (entity.getAttributes().hasAttribute(attribute)) {
            AttributeInstance inst = entity.getAttribute(attribute);

            if (inst != null) {
                boolean wasPresent = inst.hasModifier(id);
                inst.addOrReplacePermanentModifier(new AttributeModifier(id, amount, operation));

                return !wasPresent;
            }
        }

        return false;
    }
}
