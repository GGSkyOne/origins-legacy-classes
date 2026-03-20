package io.github.apace100.originsclasses.util;

public class EnchantmentContext {
    private static boolean clericEnchanting;

    public static boolean isClericEnchanting() {
        return clericEnchanting;
    }

    public static void setClericEnchanting(boolean value) {
        clericEnchanting = value;
    }
}
