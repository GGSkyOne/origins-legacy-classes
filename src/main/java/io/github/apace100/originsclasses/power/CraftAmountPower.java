package io.github.apace100.originsclasses.power;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.ValueModifyingPower;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.util.modifier.Modifier;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.originsclasses.OriginsClasses;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Predicate;

public class CraftAmountPower extends ValueModifyingPower {
    public static final PowerFactory<CraftAmountPower> FACTORY = new PowerFactory<>(
        Identifier.of(OriginsClasses.MODID, "craft_amount"),
        new SerializableData()
            .add("item_condition", ApoliDataTypes.ITEM_CONDITION, null)
            .add("modifier", Modifier.DATA_TYPE, null)
            .add("modifiers", Modifier.LIST_TYPE, null),
        data -> (type, player) -> {
            CraftAmountPower power = new CraftAmountPower(
                type,
                player,
                data.isPresent("item_condition") ? data.get("item_condition") : (stack -> true)
            );

            data.ifPresent("modifier", power::addModifier);
            data.<List<Modifier>>ifPresent("modifiers", mods -> mods.forEach(power::addModifier));

            return power;
        }
    );

    private final Predicate<ItemStack> outputPredicate;

    public CraftAmountPower(PowerType<?> type, LivingEntity entity, Predicate<ItemStack> outputPredicate) {
        super(type, entity);
        this.outputPredicate = outputPredicate;
    }

    public boolean doesApply(ItemStack outputStack) {
        return outputPredicate.test(outputStack);
    }
}
