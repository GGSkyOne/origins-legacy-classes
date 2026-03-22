package io.github.apace100.originsclasses.power;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.originsclasses.OriginsClasses;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.resources.Identifier;

public class ExplorerKitPower extends Power {
    public static final PowerFactory<ExplorerKitPower> FACTORY = new PowerFactory<>(
        Identifier.fromNamespaceAndPath(OriginsClasses.MODID, "explorer_kit"),
        new SerializableData(),
        data -> ExplorerKitPower::new
    );

    public ExplorerKitPower(PowerType<?> type, LivingEntity entity) {
        super(type, entity);
    }

    @Override
    public void onGained() {
        if (entity instanceof Player player) {
            player.getInventory().add(new ItemStack(Items.COMPASS));
            player.getInventory().add(new ItemStack(Items.CLOCK));
            player.getInventory().add(new ItemStack(Items.MAP, 9));
        }
    }
}
