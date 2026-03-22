package io.github.apace100.originsclasses.util;

import net.minecraft.world.entity.player.Player;

public class CraftingContext {
    private static Player craftingPlayer;

    public static Player getCraftingPlayer() {
        return craftingPlayer;
    }

    public static void setCraftingPlayer(Player player) {
        craftingPlayer = player;
    }
}
