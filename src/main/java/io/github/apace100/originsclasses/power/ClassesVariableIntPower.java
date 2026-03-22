package io.github.apace100.originsclasses.power;

import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.VariableIntPower;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.apace100.originsclasses.OriginsClasses;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.resources.Identifier;

public class ClassesVariableIntPower extends VariableIntPower {
    public static final PowerFactory<ClassesVariableIntPower> FACTORY = new PowerFactory<>(
        Identifier.fromNamespaceAndPath(OriginsClasses.MODID, "variable_int"),
        new SerializableData()
            .add("start_value", SerializableDataTypes.INT, null)
            .add("min", SerializableDataTypes.INT, Integer.MIN_VALUE)
            .add("max", SerializableDataTypes.INT, Integer.MAX_VALUE),
        data -> (type, entity) -> new ClassesVariableIntPower(
            type,
            entity,
            data.getInt("start_value"), data.getInt("min"), data.getInt("max")
        )
    );

    public ClassesVariableIntPower(PowerType<?> type, LivingEntity entity, int startValue, int min, int max) {
        super(type, entity, startValue, min, max);
    }
}
