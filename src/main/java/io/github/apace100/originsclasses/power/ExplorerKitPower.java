package io.github.apace100.originsclasses.power;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.originsclasses.OriginsClasses;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class ExplorerKitPower extends Power {
    public static final PowerFactory<ExplorerKitPower> FACTORY = new PowerFactory<>(
        Identifier.of(OriginsClasses.MODID, "explorer_kit"),
        new SerializableData(),
        data -> ExplorerKitPower::new
    );

    public ExplorerKitPower(PowerType<?> type, LivingEntity entity) {
        super(type, entity);
    }

    @Override
    public void onGained() {
        if (entity instanceof PlayerEntity player) {
            player.getInventory().insertStack(new ItemStack(Items.COMPASS));
            player.getInventory().insertStack(new ItemStack(Items.CLOCK));
            player.getInventory().insertStack(new ItemStack(Items.MAP, 9));
        }
    }
}
