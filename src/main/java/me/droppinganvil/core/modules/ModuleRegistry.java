/*
 * Copyright (c) DroppingAnvil 2020.
 * All Rights Reserved.
 */

package me.droppinganvil.core.modules;

import me.droppinganvil.core.Core;
import me.droppinganvil.core.factions.FactionsPlugin;
import me.droppinganvil.core.factions.modules.FactionBan;

import java.util.concurrent.ConcurrentHashMap;

public class ModuleRegistry {
    public static ConcurrentHashMap<String, CoreModule> modules = new ConcurrentHashMap<String, CoreModule>();

    static
    {
        if (Core.factionsPlugin != FactionsPlugin.None && Core.factionsPlugin != FactionsPlugin.Unknown) {
            //Load Factions core features
            modules.put("FactionBan", new FactionBan());
        }
        //Load features that do not have dependencies
        
    }
}
