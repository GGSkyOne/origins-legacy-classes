package io.github.apace100.originsclasses.util;

import io.github.apace100.originsclasses.OriginsClasses;
import net.minecraft.world.item.Item;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.Identifier;

public class ClassesTags {
    public static final TagKey<Item> MERCHANT_BLACKLIST = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(OriginsClasses.MODID, "merchant_blacklist"));
}
