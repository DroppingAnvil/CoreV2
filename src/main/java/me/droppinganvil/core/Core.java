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
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;

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

    public static HashMap<String, List<String>> parseMap(String path, Boolean lowercaseKeys) {
        HashMap<String, List<String>> parsed = new HashMap<>();
        for (String key : instance.getConfig().getConfigurationSection(path).getKeys(false)) {
            if (!lowercaseKeys) {
                parsed.put(key, instance.getConfig().getStringList(path + "." + key));
            } else {
                parsed.put(key.toLowerCase(), instance.getConfig().getStringList(path + "." + key));
            }
        }
        return parsed;
    }

    public static void adaptAndSend(List<String> message, Player p) {
        for (String s : message) {
            String temp = s;
            if (usePAPI) {
                temp = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(p, s);
            } else {
                temp = ChatColor.translateAlternateColorCodes('&', temp);
            }
            p.sendMessage(temp);
        }
    }
}
