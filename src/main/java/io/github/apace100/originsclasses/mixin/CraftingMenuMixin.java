package io.github.apace100.originsclasses.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.originsclasses.OriginsClasses;
import io.github.apace100.originsclasses.power.ClassesPowerTypes;
import io.github.apace100.originsclasses.component.ClassesComponents;
import io.github.apace100.originsclasses.power.CraftAmountPower;
import io.github.apace100.originsclasses.util.CraftingContext;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RepairItemRecipe;
import net.minecraft.core.Holder;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(CraftingMenu.class)
public class CraftingMenuMixin {
    @Unique
    private static Optional<CraftingRecipe> cachedRecipe;

    @Inject(method = "slotChangedCraftingGrid", at = @At("HEAD"))
    private static void saveCraftingPlayer(AbstractContainerMenu handler, ServerLevel world, Player player, CraftingContainer craftingInventory, ResultContainer resultInventory, @org.jspecify.annotations.Nullable RecipeHolder<CraftingRecipe> recipe, CallbackInfo ci) {
        CraftingContext.setCraftingPlayer(player);
    }

    @Inject(
        method = "slotChangedCraftingGrid",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Optional;isPresent()Z"
        )
    )
    private static void cacheRecipe(AbstractContainerMenu handler, ServerLevel world, Player player, CraftingContainer craftingInventory, ResultContainer resultInventory, @org.jspecify.annotations.Nullable RecipeHolder<CraftingRecipe> recipe, CallbackInfo ci, @Local Optional<CraftingRecipe> optional) {
        cachedRecipe = optional;
    }

    @Inject(
        method = "slotChangedCraftingGrid",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/inventory/ResultContainer;setItem(ILnet/minecraft/world/item/ItemStack;)V"
        )
    )
    private static void modifyCraftingResult(AbstractContainerMenu handler, ServerLevel world, Player player, CraftingContainer craftingInventory, ResultContainer resultInventory, @org.jspecify.annotations.Nullable RecipeHolder<CraftingRecipe> recipe, CallbackInfo ci, @Local ItemStack itemStack) {
        if (itemStack.has(DataComponents.FOOD) && ClassesPowerTypes.BETTER_CRAFTED_FOOD.isActive(player)) {
            FoodProperties food = itemStack.get(DataComponents.FOOD);

            int foodBonus = (int)Math.ceil((float)food.nutrition() / 3F);

            if (foodBonus < 1) {
                foodBonus = 1;
            }

            itemStack.set(ClassesComponents.FOOD_BONUS, foodBonus);
        }

        if (ClassesPowerTypes.QUALITY_EQUIPMENT.isActive(player) && isEquipment(itemStack)) {
            boolean recipeContainsEquipment = false;

            for (int i = 0; i < craftingInventory.getContainerSize() && !recipeContainsEquipment; i++) {
                if (isEquipment(craftingInventory.getItem(i))) {
                    recipeContainsEquipment = true;
                }
            }

            if (cachedRecipe.isPresent() && cachedRecipe.get() instanceof RepairItemRecipe) {
                recipeContainsEquipment = false;
            }

            if (!recipeContainsEquipment) {
                addQualityAttribute(itemStack);
            }
        }

        int baseValue = itemStack.getCount();
        int newValue = (int) PowerHolderComponent.modify(player, CraftAmountPower.class, baseValue, (p -> p.doesApply(itemStack)));

        if (newValue != baseValue) {
            itemStack.setCount(newValue < 0 ? 0 : Math.min(newValue, itemStack.getMaxStackSize()));
        }
    }

    @Unique
    private static void addQualityAttribute(ItemStack stack) {
        Item item = stack.getItem();

        Equippable equippable = item.components().get(DataComponents.EQUIPPABLE);

        if (equippable != null && isArmorSlot(equippable.slot())) {
            addAttributeModifier(
                stack,
                Attributes.ARMOR_TOUGHNESS,
                new AttributeModifier(
                    Identifier.fromNamespaceAndPath(OriginsClasses.MODID, "blacksmith_armor_toughness"),
                    0.25D,
                    AttributeModifier.Operation.ADD_VALUE
                ),
                EquipmentSlotGroup.bySlot(equippable.slot())
            );
        } else if (item.components().has(DataComponents.TOOL) || item instanceof ShearsItem) {
            stack.set(ClassesComponents.MINING_SPEED_MULTIPLIER, 1.05F);
        } else if (item.components().has(DataComponents.WEAPON) || item instanceof ProjectileWeaponItem) {
            addAttributeModifier(
                stack,
                Attributes.ATTACK_DAMAGE,
                new AttributeModifier(
                    Identifier.fromNamespaceAndPath(OriginsClasses.MODID, "blacksmith_attack_damage"),
                    0.5D,
                    AttributeModifier.Operation.ADD_VALUE
                ),
                EquipmentSlotGroup.MAINHAND
            );
        } else if (item instanceof ShieldItem) {
            addAttributeModifier(
                stack,
                Attributes.KNOCKBACK_RESISTANCE,
                new AttributeModifier(
                    Identifier.fromNamespaceAndPath(OriginsClasses.MODID, "blacksmith_knockback_resistance"),
                    0.1D,
                    AttributeModifier.Operation.ADD_VALUE
                ),
                EquipmentSlotGroup.OFFHAND
            );
        }
    }

    @Unique
    private static boolean isArmorSlot(EquipmentSlot slot) {
        return slot == EquipmentSlot.HEAD || slot == EquipmentSlot.CHEST || slot == EquipmentSlot.LEGS || slot == EquipmentSlot.FEET;
    }

    @Unique
    private static void addAttributeModifier(ItemStack stack, Holder<Attribute> attribute, AttributeModifier modifier, EquipmentSlotGroup slot) {
        ItemAttributeModifiers base = stack
            .getItem()
            .components()
            .get(DataComponents.ATTRIBUTE_MODIFIERS);

        if (base == null || base.modifiers().isEmpty()) {
            base = ItemAttributeModifiers.EMPTY;
        }

        List<ItemAttributeModifiers.Entry> entries = new ArrayList<>(base.modifiers());
        entries.add(new ItemAttributeModifiers.Entry(attribute, modifier, slot));

        stack.set(
            DataComponents.ATTRIBUTE_MODIFIERS,
            new ItemAttributeModifiers(entries)
        );
    }

    @Unique
    private static boolean isEquipment(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return false;
        }

        Item item = stack.getItem();

        Equippable equippable = item.components().get(DataComponents.EQUIPPABLE);

        if (equippable != null && isArmorSlot(equippable.slot()))
            return true;

        if (item.components().has(DataComponents.TOOL))
            return true;

        if (item instanceof ProjectileWeaponItem)
            return true;

        if (item instanceof ShieldItem)
            return true;

        return false;
    }
}
