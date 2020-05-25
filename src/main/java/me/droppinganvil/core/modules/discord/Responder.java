/*
 * Copyright (c) DroppingAnvil 2020.
 * All Rights Reserved.
 */

package me.droppinganvil.core.modules.discord;

import me.droppinganvil.core.Core;
import me.droppinganvil.core.modules.CoreModule;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;

public class Responder extends ListenerAdapter implements CoreModule {
    public static HashMap<String, String> responseMap = new HashMap<>();
    @Override
    public void load() {
        Discord.jda.addEventListener(this);
        for (String key : Core.instance.getConfig().getConfigurationSection("Discord.Bot").getKeys(false)) {
            String response = Core.instance.getConfig().getString("Discord.Bot." + key);
            responseMap.put(key.toLowerCase(), response);
        }
    }

    @Override
    public void disable() {

    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (responseMap.containsKey(event.getMessage().getContentDisplay().toLowerCase())) {
            String message = responseMap.get(event.getMessage().getContentDisplay().toLowerCase());
            if (Core.usePAPI) message = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(null, message);
            event.getChannel().sendMessage(message).queue();
        }
    }

}
