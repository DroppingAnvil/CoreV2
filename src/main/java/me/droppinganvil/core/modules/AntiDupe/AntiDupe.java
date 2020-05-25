/*
 * Copyright (c) DroppingAnvil 2020.
 * All Rights Reserved.
 */

package me.droppinganvil.core.modules.AntiDupe;

import de.tr7zw.nbtapi.NBTItem;
import me.droppinganvil.core.Core;
import me.droppinganvil.core.modules.CoreModule;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AntiDupe implements CoreModule, Listener {
    public static ConcurrentLinkedQueue<AsyncInventory> inventoryQueue = new ConcurrentLinkedQueue<>();
    public static Set<Player> needsChecked;

    public void load() {
        Core.instance.getServer().getPluginManager().registerEvents(this, Core.instance);
        startSyncTask();
        new Thread(new CheckThread());
    }

    public void disable() {
        CheckThread.enabled = false;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        ItemStack target = e.getCursor();
        if (target == null) return;
        needsChecked.add((Player) e.getWhoClicked());
        tagItem(target, (Player) e.getWhoClicked());
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        needsChecked.add(e.getPlayer());
        tagItem(e.getItem().getItemStack(), e.getPlayer());
    }

    private void startSyncTask() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.instance, () ->
        {
            for (Player p : needsChecked) {
                inventoryQueue.add(new AsyncInventory(p));
            }
            needsChecked.clear();
        }, 0L, Core.instance.getConfig().getLong("AntiDupe.SyncTaskInterval", 10));
    }

    public void tagItem(ItemStack i, Player target) {
        NBTItem nbti = new NBTItem(i);
        //Check for tags
        if (nbti.hasNBTData()) {
            if (nbti.hasKey("AnvilCoreID")) {
                //Already tagged
                return;
            }
        }
        nbti.setString("AnvilCoreID", System.currentTimeMillis() + "/" + target.getName());
    }
}
