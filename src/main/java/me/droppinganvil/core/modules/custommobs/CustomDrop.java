/*
 * Copyright (c) DroppingAnvil 2020.
 * All Rights Reserved.
 */

package me.droppinganvil.core.modules.custommobs;

import org.bukkit.inventory.ItemStack;

public class CustomDrop {
    public ItemStack is;
    public Integer chance;

    public CustomDrop(ItemStack is, Integer chance) {
        this.is = is;
        this.chance = chance;
    }
}
