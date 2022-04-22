package dev.thebathduck.duckypets.utils;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.material.SpawnEgg;

import java.util.ArrayList;

public class Util {
    public static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static ItemStack eggFromType(EntityType type) {
        ItemStack item = new ItemStack(Material.MONSTER_EGG);
        SpawnEggMeta meta = (SpawnEggMeta) item.getItemMeta();
        meta.setSpawnedType(type);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createVoucher(String mob, String name, String ontvanger) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(color("&aPet Voucher"));
        ArrayList<String> lore = new ArrayList<>();
        lore.add(color(""));
        lore.add(color("&7Mob: &f" + mob));
        lore.add(color("&7Naam: &f" + name));
        lore.add(color("&7Ontvanger: &f" + ontvanger));
        lore.add(color(""));
        lore.add(color("&7Rechtermuis om te claimen."));
        meta.setLore(lore);
        item.setItemMeta(meta);
        applyNBTTag(item, "mob", mob.toUpperCase());
        applyNBTTag(item, "name", name);
        return item;
    }

    public static void applyNBTTag(ItemStack itemStack, String key, Object value) {
        ItemStack is = NBTEditor.set(itemStack, value, key);
        ItemMeta itemMeta = is.getItemMeta();
        itemStack.setItemMeta(itemMeta);
    }
}
