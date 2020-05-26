/*
 * Copyright (c) DroppingAnvil 2020.
 * All Rights Reserved.
 */

package me.droppinganvil.core.modules;

import me.droppinganvil.core.Core;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.HashMap;
import java.util.List;

public class CustomCommands implements CoreModule, Listener {
    public static HashMap<String, List<String>> commands;
    @Override
    public void load() {
        commands = Core.parseMap("CustomCommands", true);
        Core.instance.getServer().getPluginManager().registerEvents(this, Core.instance);
    }

    @Override
    public void disable() {

    }

    @EventHandler
    public void onPreCommand(PlayerCommandPreprocessEvent e) {
        if (commands.containsKey(e.getMessage().toLowerCase())) {
            Core.adaptAndSend(commands.get(e.getMessage().toLowerCase()), e.getPlayer());
        }
    }
}
