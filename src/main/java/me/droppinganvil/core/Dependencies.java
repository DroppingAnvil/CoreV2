/*
 * Copyright (c) DroppingAnvil 2020.
 * All Rights Reserved.
 */

package me.droppinganvil.core;

public class Dependencies {
    public static boolean isNBTAPIInstalled() {
        return Core.instance.getServer().getPluginManager().isPluginEnabled("NBTAPI");
    }
    public static boolean isPlaceholderAPIInstalled() {
        return Core.instance.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
    }
}
