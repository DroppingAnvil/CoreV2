/*
 * Copyright (c) DroppingAnvil 2020.
 * All Rights Reserved.
 */

package me.droppinganvil.core;

import me.droppinganvil.core.factions.FactionsLoader;
import me.droppinganvil.core.factions.FactionsPlugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin {
    public static Core instance;
    public static FactionsPlugin factionsPlugin = FactionsLoader.detectAndLoad();

    public Core() {
        if (instance == null) instance = this;
    }

    public void onEnable() {

    }
}
