/*
 * Copyright (c) DroppingAnvil 2020.
 * All Rights Reserved.
 */

package me.droppinganvil.core.modules.AntiDupe;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AsyncInventory {
    private Player owner;
    private ItemStack[] inventory;

    public AsyncInventory(Player player) {
        this.owner = player;
        this.inventory = player.getInventory().getContents().clone();
    }

    public ItemStack[] getInventory() {
        return inventory;
    }

    public Player getOwner() {
        return owner;
    }
}
