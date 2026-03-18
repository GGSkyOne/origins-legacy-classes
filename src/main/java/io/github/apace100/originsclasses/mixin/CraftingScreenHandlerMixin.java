package io.github.apace100.originsclasses.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.originsclasses.OriginsClasses;
import io.github.apace100.originsclasses.power.ClassPowerTypes;
import io.github.apace100.originsclasses.component.ClassesComponents;
import io.github.apace100.originsclasses.power.CraftAmountPower;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.*;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RepairItemRecipe;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(CraftingScreenHandler.class)
public class CraftingScreenHandlerMixin {
    @Unique
    private static Optional<CraftingRecipe> classes$CachedRecipe;

    @Inject(method = "updateResult", at = @At(value = "INVOKE", target = "Ljava/util/Optional;isPresent()Z"), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void cacheRecipe(ScreenHandler handler, World world, PlayerEntity player, RecipeInputInventory craftingInventory, CraftingResultInventory resultInventory, RecipeEntry<CraftingRecipe> recipe, CallbackInfo ci, CraftingRecipeInput craftingRecipeInput, ServerPlayerEntity serverPlayerEntity, ItemStack itemStack, Optional optional) {
        classes$CachedRecipe = optional;
    }

    @Inject(method = "updateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/CraftingResultInventory;setStack(ILnet/minecraft/item/ItemStack;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void modifyCraftingResult(ScreenHandler handler, World world, PlayerEntity player, RecipeInputInventory craftingInventory, CraftingResultInventory resultInventory, CallbackInfo ci, ServerPlayerEntity serverPlayerEntity, ItemStack itemStack) {
        if (itemStack.contains(DataComponentTypes.FOOD) && ClassPowerTypes.BETTER_CRAFTED_FOOD.isActive(player)) {
            FoodComponent food = itemStack.get(DataComponentTypes.FOOD);

            int foodBonus = (int)Math.ceil((float)food.nutrition() / 3F);

            if(foodBonus < 1) {
                foodBonus = 1;
            }

            itemStack.set(ClassesComponents.FOOD_BONUS, foodBonus);
        }

        if (ClassPowerTypes.QUALITY_EQUIPMENT.isActive(player) && isEquipment(itemStack)) {
            boolean recipeContainsEquipment = false;

            for (int i = 0; i < craftingInventory.size() && !recipeContainsEquipment; i++) {
                if (isEquipment(craftingInventory.getStack(i))) {
                    recipeContainsEquipment = true;
                }
            }

            if (classes$CachedRecipe.isPresent() && classes$CachedRecipe.get() instanceof RepairItemRecipe) {
                recipeContainsEquipment = false;
            }

            if (!recipeContainsEquipment) {
                addQualityAttribute(itemStack);
            }
        }

        int baseValue = itemStack.getCount();
        int newValue = (int) PowerHolderComponent.modify(player, CraftAmountPower.class, baseValue, (p -> p.doesApply(itemStack)));

        if(newValue != baseValue) {
            itemStack.setCount(newValue < 0 ? 0 : Math.min(newValue, itemStack.getMaxCount()));
        }
    }

    @Unique
    private static void addQualityAttribute(ItemStack stack) {
        Item item = stack.getItem();

        if (item instanceof ArmorItem armor) {
            addAttributeModifier(
                stack,
                EntityAttributes.GENERIC_ARMOR_TOUGHNESS,
                new EntityAttributeModifier(
                    Identifier.of(OriginsClasses.MODID, "blacksmith_armor_toughness"),
                    0.25D,
                    EntityAttributeModifier.Operation.ADD_VALUE
                ),
                AttributeModifierSlot.forEquipmentSlot(armor.getSlotType())
            );
        } else if (item instanceof SwordItem || item instanceof RangedWeaponItem) {
            addAttributeModifier(
                stack,
                EntityAttributes.GENERIC_ATTACK_DAMAGE,
                new EntityAttributeModifier(
                    Identifier.of(OriginsClasses.MODID, "blacksmith_attack_damage"),
                    0.5D,
                    EntityAttributeModifier.Operation.ADD_VALUE
                ),
                AttributeModifierSlot.MAINHAND
            );
        } else if (item instanceof MiningToolItem || item instanceof ShearsItem) {
            stack.set(ClassesComponents.MINING_SPEED_MULTIPLIER, 1.05F);
        } else if (item instanceof ShieldItem) {
            addAttributeModifier(
                stack,
                EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE,
                new EntityAttributeModifier(
                    Identifier.of(OriginsClasses.MODID, "blacksmith_knockback_resistance"),
                    0.1D,
                    EntityAttributeModifier.Operation.ADD_VALUE
                ),
                AttributeModifierSlot.OFFHAND
            );
        }
    }

    @Unique
    private static void addAttributeModifier(ItemStack stack, RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier, AttributeModifierSlot slot) {
        AttributeModifiersComponent old = stack.getOrDefault(
            DataComponentTypes.ATTRIBUTE_MODIFIERS,
            AttributeModifiersComponent.DEFAULT
        );

        List<AttributeModifiersComponent.Entry> entries = new ArrayList<>(old.modifiers());
        entries.add(new AttributeModifiersComponent.Entry(attribute, modifier, slot));

        stack.set(
            DataComponentTypes.ATTRIBUTE_MODIFIERS,
            new AttributeModifiersComponent(entries, old.showInTooltip())
        );
    }

    @Unique
    private static boolean isEquipment(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return false;
        }

        Item item = stack.getItem();

        if(item instanceof ArmorItem)
            return true;

        if(item instanceof ToolItem)
            return true;

        if(item instanceof RangedWeaponItem)
            return true;

        if (item instanceof ShieldItem)
            return true;

        return false;
    }
}
