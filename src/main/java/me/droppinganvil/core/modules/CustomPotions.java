/*
 * Copyright (c) DroppingAnvil 2020.
 * All Rights Reserved.
 */

package me.droppinganvil.core.modules;

import de.tr7zw.nbtapi.NBTItem;
import me.droppinganvil.core.Core;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CustomPotions implements CoreModule, Listener, CommandExecutor {
    @Override
    public void load() {
        Core.instance.getServer().getPluginManager().registerEvents(this, Core.instance);
        Core.instance.getCommand("potions").setExecutor(this);
    }

    @Override
    public void disable() {

    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length < 3) {
            Core.sendSyntaxError("/potion {player} {potion name} {amount}", commandSender);
            return true;
        }
        if (!commandSender.hasPermission("Core.Potions")) {
            Core.sendNoPermission("Give Potions", commandSender);
            return true;
        }
        Player p = Bukkit.getPlayer(strings[0]);
        if (p == null) {
            Core.sendPlayerNotFound(strings[0], commandSender);
            return true;
        }
        if (Core.instance.getConfig().getString("CustomPotions." + strings[1] + ".Name") == null) {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.instance.getConfig().getString("Messages.PotionNotFound", "&c&lError: Potion not found!")));
            return true;
        }
        int amount;
        try {
            amount = Integer.parseInt(strings[2]);
        } catch (Exception e) {
            //Lets just hope its that exception I just forgot what it was and cant be asked to pull up google or go to the method
            Core.sendSyntaxError("/potion {player} {potion name} {amount}", commandSender);
            return true;
        }
        p.getInventory().addItem(buildPotion(strings[1], amount, p));

        return true;
    }

    @EventHandler
    public void onPotion(PotionSplashEvent e) {
        NBTItem nbti = new NBTItem(e.getPotion().getItem());
        if (nbti.hasKey("CustomPotion")) {
            for (String s : Core.instance.getConfig().getStringList("CustomPotion." + nbti.getString("CustomPotion") + ".Commands")) {
                if (s.contains("{affected}")) {
                    for (Entity entity : e.getAffectedEntities()) {
                        if (entity instanceof Player) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replace("{affected}", entity.getName()));
                        }
                    }
                }
            }
        }
    }

    public static ItemStack buildPotion(String name, Integer amount, Player owner) {
        PotionType potionType = PotionType.valueOf(Core.instance.getConfig().getString("CustomPotions." + name + ".Type", "WATER"));
        int level = Core.instance.getConfig().getInt("CustomPotions." + name + ".Level", 1);
        boolean extended = Core.instance.getConfig().getBoolean("CustomPotions." + name + ".Extended", false);
        boolean stackable = Core.instance.getConfig().getBoolean("CustomPotions." + name + ".Stackable", false);
        ItemStack potionStack;
        // 1.8 or earlier
        //TODO Replace exception catching with version search
        try {
            Potion potion = new Potion(potionType, level, true, extended);
            potionStack = potion.toItemStack(amount);
        } catch (Exception e) {
            // 1.9 or later
            potionStack = new ItemStack(Material.SPLASH_POTION, amount);
            org.bukkit.inventory.meta.PotionMeta pm = (org.bukkit.inventory.meta.PotionMeta) potionStack.getItemMeta();
            pm.setBasePotionData(new PotionData(potionType, extended, false));
            pm.setColor(Color.fromBGR(Core.instance.getConfig().getInt("CustomPotions." + name + ".Color.Blue", 50), Core.instance.getConfig().getInt("CustomPotions." + name + ".Color.Green", 50), Core.instance.getConfig().getInt("CustomPotions." + name + ".Color.Red", 50)));
            potionStack.setItemMeta(pm);
        }
        ItemMeta meta = potionStack.getItemMeta();
        String itemName = Core.instance.getConfig().getString("CustomPotions." + name + ".Name", "&bDefault Potion");
        if (Core.usePAPI) {
            itemName = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(owner, itemName, true);
        } else {
            itemName = ChatColor.translateAlternateColorCodes('&', itemName);
        }
        meta.setDisplayName(itemName);
        List<String> lore = new ArrayList<>();
        for (String s : Core.instance.getConfig().getStringList("CustomPotions." + name + ".Lore")) {
            String temp = s;
            if (Core.usePAPI) {
                temp = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(owner, temp, true);
            } else {
                temp = ChatColor.translateAlternateColorCodes('&', temp);
            }
            lore.add(temp);
        }
        meta.setLore(lore);
        potionStack.setItemMeta(meta);
        NBTItem nbti = new NBTItem(potionStack);
        nbti.setBoolean("Stackable", stackable);
        nbti.setString("CustomPotion", name);
        return nbti.getItem();
    }
}
