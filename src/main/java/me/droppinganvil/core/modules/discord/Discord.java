/*
 * Copyright (c) DroppingAnvil 2020.
 * All Rights Reserved.
 */

package me.droppinganvil.core.modules.discord;

import com.mysql.cj.xdevapi.Collection;
import com.mysql.cj.xdevapi.Session;
import com.mysql.cj.xdevapi.SessionFactory;
import com.mysql.cj.xdevapi.Table;
import me.droppinganvil.core.Core;
import me.droppinganvil.core.modules.CoreModule;
import me.droppinganvil.core.modules.ModuleRegistry;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.entity.Player;

import javax.security.auth.login.LoginException;
import java.util.logging.Level;

public class Discord implements CoreModule {
    public static JDA jda;
    public boolean useMySQL = false;

    static {
        try {
            jda = new JDABuilder(Core.instance.getConfig().getString("Discord.Token")).build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    public Discord() {

    }

    @Override
    public void load() {
        if (jda != null) {
            Status status = new Status();
            ModuleRegistry.modules.put("Status", status);
            status.load();
            Responder responder = new Responder();
            ModuleRegistry.modules.put("Responder", responder);
            responder.load();
        } else {
            Core.instance.getLogger().log(Level.INFO, "Discord modules disabled due to invalid bot token.");
        }
    }

    @Override
    public void disable() {
        jda.shutdown();
    }
}
