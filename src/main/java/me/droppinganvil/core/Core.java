/*
 * Copyright (c) DroppingAnvil 2020.
 * All Rights Reserved.
 */

package me.droppinganvil.core;

import me.droppinganvil.core.factions.FactionsLoader;
import me.droppinganvil.core.factions.FactionsPlugin;
import me.droppinganvil.core.modules.CoreModule;
import me.droppinganvil.core.modules.ModuleRegistry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin {
    public static Core instance;
    public static FactionsPlugin factionsPlugin = FactionsLoader.detectAndLoad();
    public static boolean useNBTAPI = Dependencies.isNBTAPIInstalled();
    public static boolean usePAPI = Dependencies.isPlaceholderAPIInstalled();

    public Core() {
        if (instance == null) instance = this;
    }

    public void onEnable() {
        saveDefaultConfig();
        for (CoreModule cm : ModuleRegistry.modules.values()) {
            cm.load();
        }
    }

    public void onDisable() {
        for (CoreModule cm : ModuleRegistry.modules.values()) {
            cm.disable();
        }
    }

    public static void runConsoleCommands(String key, Player target) {
        for (String s : instance.getConfig().getStringList(key)) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s
                    .replace("{player}", target.getName())
                    //If needed add more placeholders here
            );
        }
    }
}
