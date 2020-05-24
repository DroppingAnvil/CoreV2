/*
 * Copyright (c) DroppingAnvil 2020.
 * All Rights Reserved.
 */

package me.droppinganvil.core.factions.modules;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.event.FactionDisbandEvent;
import me.droppinganvil.core.Core;
import me.droppinganvil.core.factions.FactionsPlugin;
import me.droppinganvil.core.modules.CoreModule;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import java.util.Set;

public class FactionBan implements CoreModule, Listener {
    public void load() {
        Core.instance.getServer().getPluginManager().registerEvents(this, Core.instance);
    }

    public void disable() {

    }
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().contains("ban") && e.getMessage().contains("-f")) {
            String target = e.getMessage().split(" ")[1];
            Set<FPlayer> fplayers;
            Faction faction = Factions.getInstance().getByTag(target);
            if (faction != null) {
                fplayers = getFactionMembers(faction, e);
            } else {
                Player player = Bukkit.getPlayer(target);
                if (player == null) {
                    returnNoTarget(e);
                    return;
                }
                FPlayer fplayer = FPlayers.getInstance().getByPlayer(player);
                if (fplayer == null) {
                    returnNoTarget(e);
                    return;
                }
                fplayers = getFactionMembers(fplayer.getFaction(), e);
            }
            if (fplayers == null) return;
            for (FPlayer fplayer : fplayers) {
                Bukkit.dispatchCommand(e.getPlayer(), e.getMessage().replace(target, fplayer.getName()).replace("-f", ""));
            }
            e.setCancelled(true);
        }
    }
    public Set<FPlayer> getFactionMembers(Faction faction, PlayerCommandPreprocessEvent e) {
        Set<FPlayer> fplayers;
        if (faction.isWilderness()) {
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', Core.instance.getConfig().getString("Messages.NoFaction", "&c&lError: The faction you attempted to ban is Wilderness!")));
            return null;
        }
            fplayers = faction.getFPlayers();
            if (Core.factionsPlugin == FactionsPlugin.SaberFactions) fplayers.addAll(faction.getAltPlayers());
        if (Core.instance.getConfig().getBoolean("FactionBan.Disband", false)) faction.disband(e.getPlayer(), FactionDisbandEvent.PlayerDisbandReason.COMMAND);
        return fplayers;
    }
    public static void returnNoTarget(PlayerCommandPreprocessEvent e) {
        e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', Core.instance.getConfig().getString("Messages.TargetNotFound", "&c&lError: Target could not be found please use a valid faction tag or player name!")));
    }
}
