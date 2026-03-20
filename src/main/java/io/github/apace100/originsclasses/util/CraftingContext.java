package io.github.apace100.originsclasses.util;

import net.minecraft.entity.player.PlayerEntity;

public class CraftingContext {
    private static PlayerEntity craftingPlayer;

    public static PlayerEntity get() {
        return craftingPlayer;
    }

    public static void set(PlayerEntity player) {
        craftingPlayer = player;
    }
}
