/*
 * Copyright (c) DroppingAnvil 2020.
 * All Rights Reserved.
 */

package me.droppinganvil.core;

import me.droppinganvil.core.factions.FactionsLoader;
import me.droppinganvil.core.factions.FactionsPlugin;
import me.droppinganvil.core.impl.MCVersion;
import me.droppinganvil.core.modules.CoreModule;
import me.droppinganvil.core.modules.ModuleRegistry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;

public class Core extends JavaPlugin {
    public static Core instance;
    public static FactionsPlugin factionsPlugin;
    public static boolean useNBTAPI;
    public static boolean usePAPI;
    public static String noPermission;
    public static String notEnoughArgs;
    public static String playerNotFound;
    public static MCVersion minecraftVersion;

    public Core() {
        if (instance == null) instance = this;
    }

    public void onEnable() {
        saveDefaultConfig();
        factionsPlugin = FactionsLoader.detectAndLoad(this);
        useNBTAPI = Dependencies.isNBTAPIInstalled();
        usePAPI = Dependencies.isPlaceholderAPIInstalled();
        for (CoreModule cm : ModuleRegistry.modules.values()) {
            cm.load();
        }
        noPermission = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.NoPermission", "&c&lError: You do not have permission to {feature}"));
        notEnoughArgs = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.NotEnoughArgs", "&c&lError: Syntax error. Use the command like this {command}"));
        playerNotFound = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.PlayerNotFound", "&c&lError: a player with the name '{name}' could not be found!"));
        //Version finding
        String bukkitVersion = getServer().getBukkitVersion();
        for (MCVersion version : MCVersion.values()) {
            if (bukkitVersion.contains(version.name())) {
                minecraftVersion = version;
                break;
            }
        }
        if (minecraftVersion == null) {
            //TODO Actually do this correctly by parsing the string I just dont be knowing what it'll be
            minecraftVersion = MCVersion.Latest;
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

    public static void sendNoPermission(String feature, CommandSender commandSender) {
        commandSender.sendMessage(noPermission.replace("{feature}", feature));
    }

    public static void sendSyntaxError(String command, CommandSender commandSender) {
        commandSender.sendMessage(notEnoughArgs.replace("{command}", command));
    }

    public static void sendPlayerNotFound(String player, CommandSender commandSender) {
        commandSender.sendMessage(playerNotFound.replace("{name}", player));
    }
}
