/*
 * Copyright (c) DroppingAnvil 2020.
 * All Rights Reserved.
 */

package me.droppinganvil.core;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.io.IOException;

public class CoreListeners implements Listener {
    @EventHandler
    public void onSpawn(PlayerLoginEvent e) throws IllegalAccessException, IOException, InstantiationException {
            Player p = e.getPlayer();
            SeamlessPlayer.playerMap.put(p.getUniqueId().toString(), (SeamlessPlayer) Core.seamlessPlayerSQL.getObject("uuid", p.getUniqueId().toString(), SeamlessPlayer.class));
    }
}
