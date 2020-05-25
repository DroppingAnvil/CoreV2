/*
 * Copyright (c) DroppingAnvil 2020.
 * All Rights Reserved.
 */

package me.droppinganvil.core.modules.AntiDupe;

import de.tr7zw.nbtapi.NBTItem;
import me.droppinganvil.core.Core;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CheckThread implements Runnable {
    public static boolean enabled = true;
    public void run() {
        while (enabled) {
            AsyncInventory target = AntiDupe.inventoryQueue.poll();
            if (target == null) {
                try {
                    Thread.sleep(Core.instance.getConfig().getInt("AntiDupe.AsyncTaskSleep", 20));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                List<String> ids = new ArrayList<>();
                for (ItemStack itemStack : target.getInventory()) {
                    NBTItem nbti = new NBTItem(itemStack);
                    if (nbti.hasNBTData()) {
                        if (nbti.hasKey("AnvilCoreID")) {
                            String id = nbti.getString("AnvilCoreID");
                            if (ids.contains(id)) {
                                Core.runConsoleCommands("AntiDupe.PunishmentCommands", target.getOwner());
                                return;
                            }
                            ids.add(id);
                        }
                    }
                }
            }
        }
    }
}
