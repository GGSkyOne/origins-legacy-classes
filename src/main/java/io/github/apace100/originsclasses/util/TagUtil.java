package io.github.apace100.originsclasses.util;

import net.minecraft.core.Registry;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;

import java.util.HashSet;
import java.util.Set;

public final class TagUtil {
    public static <T> Set<T> getAllEntries(Registry<T> registry, TagKey<T> tag) {
        Set<T> entrySet = new HashSet<>();

        for (Holder<T> entry :
            registry.getTagOrEmpty(tag)) {
            entrySet.add(entry.value());
        }

        return entrySet;
    }
}
