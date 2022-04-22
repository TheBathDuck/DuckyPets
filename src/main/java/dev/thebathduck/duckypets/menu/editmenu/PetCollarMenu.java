package dev.thebathduck.duckypets.menu.editmenu;

import dev.thebathduck.duckypets.Duckypets;
import dev.thebathduck.duckypets.objects.Pet;
import dev.thebathduck.duckypets.utils.GUIHolder;
import dev.thebathduck.duckypets.utils.ItemBuilder;
import dev.thebathduck.duckypets.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Wolf;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Dye;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Wool;

public class PetCollarMenu extends GUIHolder {
    public PetCollarMenu(){}

    public void open(Player player, Pet pet) {
        this.inventory = Bukkit.createInventory(this, 3*9, Util.color("Pet Bewerken | " + pet.getName()));

        for(DyeColor color : DyeColor.values()) {
            Wool wool = new Wool(color);
            ItemStack item = wool.toItemStack(1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(fromDyeColor(color) + color.toString().toUpperCase());
            item.setItemMeta(meta);
            this.inventory.addItem(item);

        }

        player.openInventory(this.inventory);
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if(event.getCurrentItem() == null) return;
        if(event.getCurrentItem().getType().equals(Material.AIR)) return;
        Player player = (Player) event.getWhoClicked();
        Wool wool = (Wool) event.getCurrentItem().getData();
        DyeColor color = wool.getColor();
        Pet pet = Duckypets.getOwnerPets().get(player);
        if(pet.getEntity() instanceof Wolf) {
            pet.setColarColor(color);
            player.sendMessage(Util.color("&aJe hebt de halsband kleur veranderd naar " + fromDyeColor(color) + "deze &akleur."));
            player.closeInventory();
        }
        if(pet.getEntity() instanceof Sheep) {
            pet.setSheepColor(color);
            player.sendMessage(Util.color("&aJe hebt de wol kleur veranderd naar " + fromDyeColor(color) + "deze &akleur."));
            player.closeInventory();
        }
    }

    private ChatColor fromDyeColor(DyeColor dyeColor) {
        String stringColor = dyeColor.toString().toUpperCase();
        switch (stringColor) {
            case "ORANGE":
                return ChatColor.GOLD;
            case "MAGENTA":
                return  ChatColor.LIGHT_PURPLE;
            case "LIGHT_BLUE":
                return ChatColor.AQUA;
            case "LIME":
                return ChatColor.GREEN;
            case "PINK":
                return ChatColor.LIGHT_PURPLE;
            case "SILVER":
                return ChatColor.GRAY;
            case "CYAN":
                return ChatColor.BLUE;
            case "PURPLE":
                return ChatColor.DARK_PURPLE;
            case "BROWN":
                return ChatColor.GOLD;
        }

        return ChatColor.valueOf(stringColor);
    }
}
