/*
 * Copyright (c) DroppingAnvil 2020.
 * All Rights Reserved.
 */

package me.droppinganvil.core;

import me.droppinganvil.core.mysql.annotations.Key;
import me.droppinganvil.core.mysql.annotations.MemoryOnly;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class SeamlessPlayer {
    @MemoryOnly
    public static HashMap<String, SeamlessPlayer> playerMap = new HashMap<>();
    public SeamlessPlayer() {}
    @Key
    public String uuid;
    @MemoryOnly
    public Player player;
    public Integer points = 0;
}
