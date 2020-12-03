/*
 * Copyright (c) DroppingAnvil 2020.
 * All Rights Reserved.
 */

package me.droppinganvil.core.modules.custommobs;

import me.droppinganvil.core.Core;
import me.droppinganvil.core.modules.CoreModule;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomMobs implements CoreModule, Listener {
    public static HashMap<EntityType, List<CustomMob>> mobs = new HashMap<>();
    @Override
    public void load() {

    }

    @Override
    public void disable() {

    }

    public void parseConf() {
        FileConfiguration conf = Core.instance.getConfig();
        for (String key : conf.getConfigurationSection("CustomMobs").getKeys(false)) {
            ConfigurationSection cs = conf.getConfigurationSection("CustomMobs." + key);
            String name = cs.getString("Name");
            Integer chance = cs.getInt("Chance");
            EntityType et = EntityType.valueOf(cs.getString("Entity"));
            ItemStack weapon = new ItemStack(Material.valueOf(cs.getString("Weapon.Material")));
            for (String enchs : cs.getStringList("Weapon.Enchantments")) {
                String[] encha = enchs.split(",");
                weapon.addEnchantment(Enchantment.getByName(encha[0]), Integer.parseInt(encha[1]));
            }
            List<CustomDrop> drops = new ArrayList<>();
            for (String dropk : cs.getConfigurationSection("Drops").getKeys(false)) {
                ConfigurationSection dropS = cs.getConfigurationSection("Drops." + dropk);
                ItemStack drop = new ItemStack(Material.getMaterial(dropS.getString("Material")), dropS.getInt("Amount"));
                ItemMeta dropMeta = drop.getItemMeta();
                dropMeta.setLore(dropS.getStringList("Lore"));
                drop.setItemMeta(dropMeta);
                for (String enchs : dropS.getStringList("Enchantments")) {
                    String[] encha = enchs.split(",");
                    drop.addEnchantment(Enchantment.getByName(encha[0]), Integer.parseInt(encha[1]));
                }
                drops.add(new CustomDrop(drop, dropS.getInt("Chance")));
            }
            CustomMob cm = new CustomMob(et, name, chance, weapon, drops);
            if (mobs.containsKey(et)) {
                mobs.get(et).add(cm);
            } else {
                List<CustomMob> cml = new ArrayList<>();
                cml.add(cm);
                mobs.put(et, cml);
            }
        }
    }
    @EventHandler
    public void onMobSpawn(EntitySpawnEvent e) {
        //EntityType
    }
}
