package io.github.apace100.originsclasses.compat;

import io.github.apace100.originsclasses.component.ClassesComponents;
import net.minecraft.world.food.FoodProperties;
import squeek.appleskin.api.AppleSkinApi;
import squeek.appleskin.api.event.FoodValuesEvent;

public class AppleSkinCompat implements AppleSkinApi {
    @Override
    public void registerEvents() {
        FoodValuesEvent.EVENT.register(event -> {
            Integer foodBonus = event.itemStack.get(ClassesComponents.FOOD_BONUS);
            if (foodBonus == null) return;

            FoodProperties original = event.modifiedFoodComponent;
            if (original == null) return;

            int newNutrition = original.nutrition() + foodBonus;

            float origAbsSaturation = original.saturation();
            float bonusAbsSaturation = (float) foodBonus * ((float) foodBonus * 0.2F) * 2F;
            float newSaturationModifier = (origAbsSaturation + bonusAbsSaturation) / (newNutrition * 2F);

            FoodProperties.Builder builder = new FoodProperties.Builder()
                .nutrition(newNutrition)
                .saturationModifier(newSaturationModifier);

            if (original.canAlwaysEat()) builder.alwaysEdible();

            event.modifiedFoodComponent = builder.build();
        });
    }
}
