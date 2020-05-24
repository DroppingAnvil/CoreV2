/*
 * Copyright (c) DroppingAnvil 2020.
 * All Rights Reserved.
 */

package me.droppinganvil.core.factions;

import me.droppinganvil.core.Core;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class FactionsLoader {
    public static FactionsPlugin detectAndLoad() {
        PluginManager pm = Core.instance.getServer().getPluginManager();
        if (pm.isPluginEnabled("Factions")) {
            Plugin factionsPlugin = pm.getPlugin("Factions");
            if (factionsPlugin.getDescription().getAuthors().contains("DroppingAnvil")) return FactionsPlugin.SaberFactions;
            if (factionsPlugin.getDescription().getAuthors().contains("ProSavage")) return FactionsPlugin.SavageFactions;
            if (factionsPlugin.getDescription().getAuthors().contains("drtshock")) return FactionsPlugin.FactionsUUID;
            return FactionsPlugin.Unknown;
        } else {
            return FactionsPlugin.None;
        }
    }
}
