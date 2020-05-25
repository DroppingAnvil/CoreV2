/*
 * Copyright (c) DroppingAnvil 2020.
 * All Rights Reserved.
 */

package me.droppinganvil.core.modules.discord;

import me.droppinganvil.core.Core;
import me.droppinganvil.core.modules.CoreModule;
import net.dv8tion.jda.api.entities.Activity;
import org.bukkit.Bukkit;

public class Status implements CoreModule {
    @Override
    public void load() {
        startSyncTask();
    }

    @Override
    public void disable() {

    }

    private void startSyncTask() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.instance, this::updateStatus, 0L, Core.instance.getConfig().getLong("Discord.Status.UpdateInterval", 30));
    }

    public void updateStatus() {
        String path = Core.instance.getServer().hasWhitelist() ? "Whitelisted" : "Open";
        String message = Core.instance.getConfig().getString("Discord.Status." + path + ".Message", "Error");
        if (Core.usePAPI) message = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(null, message);
        Discord.jda.getPresence().setPresence(Activity.of(Activity.ActivityType.valueOf("Discord.Status." + path + ".Activity"), message), true);
    }
}
