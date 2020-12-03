/*
 * Copyright (c) DroppingAnvil 2020.
 * All Rights Reserved.
 */

package me.droppinganvil.core.modules.custommobs;

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class CustomMob {
    public EntityType et;
    public String name;
    public Integer chance;
    public ItemStack weapon;
    public Collection<CustomDrop> drops;
    public CustomMob(EntityType entityType, String name, Integer chance, ItemStack weapon, Collection<CustomDrop> drops) {
        this.et = entityType;
        this.name = name;
        this.chance = chance;
        this.weapon = weapon;
        this.drops = drops;
    }
}
