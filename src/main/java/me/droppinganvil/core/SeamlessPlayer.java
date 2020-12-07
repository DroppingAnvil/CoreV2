/*
 * Copyright (c) DroppingAnvil 2020.
 * All Rights Reserved.
 */

package me.droppinganvil.core;

import me.droppinganvil.core.mysql.annotations.Key;
import me.droppinganvil.core.mysql.annotations.MemoryOnly;
import org.bukkit.entity.Player;

public class SeamlessPlayer {
    @Key
    public String uuid;
    @MemoryOnly
    public Player player;
    public Integer points = 0;
}
