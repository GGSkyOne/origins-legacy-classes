package io.github.apace100.originsclasses.power;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeReference;
import io.github.apace100.apoli.power.VariableIntPower;
import io.github.apace100.originsclasses.OriginsClasses;
import net.minecraft.util.Identifier;

public class ClassesPowerTypes {
    /* Farmer
    * MORE_CROP_DROPS - When harvesting crops, there is a chance that you receive twice the yield.
    * BETTER_BONE_MEAL - Bone meal is twice as effective in your experienced hands when used on crops and plants.
    */
    public static final PowerType<Power> MORE_CROP_DROPS = new PowerTypeReference<>(Identifier.of(OriginsClasses.MODID,"more_crop_drops"));
    public static final PowerType<Power> BETTER_BONE_MEAL = new PowerTypeReference<>(Identifier.of(OriginsClasses.MODID,"better_bone_meal"));

    /* Rancher
    * TWIN_BREEDING - Animals bred by you have a chance to produce two babies.
    * MORE_ANIMAL_LOOT - You are able to sometimes receive more material from killing animals.
    */
    public static final PowerType<Power> TWIN_BREEDING = new PowerTypeReference<>(Identifier.of(OriginsClasses.MODID,"twin_breeding"));
    public static final PowerType<Power> MORE_ANIMAL_LOOT = new PowerTypeReference<>(Identifier.of(OriginsClasses.MODID,"more_animal_loot"));

    /* Miner
    * NO_MINING_EXHAUSTION - Breaking blocks doesn't cause you to exhaust.
    */
    public static final PowerType<Power> NO_MINING_EXHAUSTION = new PowerTypeReference<>(Identifier.of(OriginsClasses.MODID,"no_mining_exhaustion"));

    /* Cook
    * MORE_SMOKER_XP - You receive more experience from cooking food in a smoker.
    * BETTER_CRAFTED_FOOD - Food crafted by you is more saturating.
    */
    public static final PowerType<Power> MORE_SMOKER_XP = new PowerTypeReference<>(Identifier.of(OriginsClasses.MODID,"more_smoker_xp"));
    public static final PowerType<Power> BETTER_CRAFTED_FOOD = new PowerTypeReference<>(Identifier.of(OriginsClasses.MODID,"better_crafted_food"));

    /* Blacksmith
    * QUALITY_EQUIPMENT - Equipment you create provides small buffs.
    * EFFICIENT_REPAIRS - Repairing equipment in an anvil costs less material. Repairing by combining equipment restores more durability.
    */
    public static final PowerType<Power> QUALITY_EQUIPMENT = new PowerTypeReference<>(Identifier.of(OriginsClasses.MODID,"quality_equipment"));
    public static final PowerType<Power> EFFICIENT_REPAIRS = new PowerTypeReference<>(Identifier.of(OriginsClasses.MODID,"efficient_repairs"));

    /* Cleric
    * LONGER_POTIONS - As a last step in potion brewing, you can double a potion's duration with water from a cauldron.
    * BETTER_ENCHANTING - You are able to produce better enchantments at an enchantment table.
    */
    public static final PowerType<Power> LONGER_POTIONS = new PowerTypeReference<>(Identifier.of(OriginsClasses.MODID,"longer_potions"));
    public static final PowerType<Power> BETTER_ENCHANTING = new PowerTypeReference<>(Identifier.of(OriginsClasses.MODID,"better_enchanting"));

    /* Merchant
    * TRADE_AVAILABILITY - Villagers you trade with never run out of resources to trade for you.
    * RARE_WANDERING_LOOT - You are able to convince wandering traders to offer some of their rarer items to you.
    */
    public static final PowerType<Power> TRADE_AVAILABILITY = new PowerTypeReference<>(Identifier.of(OriginsClasses.MODID,"trade_availability"));
    public static final PowerType<Power> RARE_WANDERING_LOOT = new PowerTypeReference<>(Identifier.of(OriginsClasses.MODID,"rare_wandering_loot"));

    /* Explorer
    * NO_SPRINT_EXHAUSTION - Sprinting doesn't cause you to exhaust.
    */
    public static final PowerType<Power> NO_SPRINT_EXHAUSTION = new PowerTypeReference<>(Identifier.of(OriginsClasses.MODID,"no_sprint_exhaustion"));

    /* Warrior
    * LESS_SHIELD_SLOWDOWN - You are slowed down less when protecting yourself with a shield.
    */
    public static final PowerType<Power> LESS_SHIELD_SLOWDOWN = new PowerTypeReference<>(Identifier.of(OriginsClasses.MODID,"less_shield_slowdown"));

    /* Archer
    * LESS_BOW_SLOWDOWN - You can move quicker than others while drawing your bow.
    * NO_PROJECTILE_DIVERGENCE - All of your projectiles have increased accuracy.
    */
    public static final PowerType<Power> LESS_BOW_SLOWDOWN = new PowerTypeReference<>(Identifier.of(OriginsClasses.MODID,"less_bow_slowdown"));
    public static final PowerType<Power> NO_PROJECTILE_DIVERGENCE = new PowerTypeReference<>(Identifier.of(OriginsClasses.MODID,"no_projectile_divergence"));

    /* Rogue
    * SNEAKY - Your nameplate is never visible through walls, even when you're not sneaking
    * STEALTH - When you have been sneaking for 10 seconds, you enter Stealth.
    */
    public static final PowerType<Power> SNEAKY = new PowerTypeReference<>(Identifier.of(OriginsClasses.MODID,"sneaky"));
    public static final PowerType<VariableIntPower> STEALTH = new PowerTypeReference<>(Identifier.of(OriginsClasses.MODID,"stealth"));

    /* Beastmaster
    * TAMED_ANIMAL_BOOST - Animals you tame receive a permanent buff to their health and strength.
    * TAMED_POTION_DIFFUSAL - Your nearby tamed animals also receive potion effects when you drink a potion.
    */
    public static final PowerType<Power> TAMED_ANIMAL_BOOST = new PowerTypeReference<>(Identifier.of(OriginsClasses.MODID,"tamed_animal_boost"));
    public static final PowerType<Power> TAMED_POTION_DIFFUSAL = new PowerTypeReference<>(Identifier.of(OriginsClasses.MODID,"tamed_potion_diffusal"));
}
