package dev.thebathduck.duckypets.menu.editmenu;

import dev.thebathduck.duckypets.Duckypets;
import dev.thebathduck.duckypets.objects.Pet;
import dev.thebathduck.duckypets.utils.GUIHolder;
import dev.thebathduck.duckypets.utils.Util;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

import java.util.Locale;

public class PetCatMenu extends GUIHolder {
    public PetCatMenu(){}

    public void open(Player player, Pet pet) {
        this.inventory = Bukkit.createInventory(this, 3*9, Util.color("Pet Bewerken | " + pet.getName()));

        for(Ocelot.Type type : Ocelot.Type.values()) {
            ItemStack item = new ItemStack(Material.WOOL);
            item.setDurability((short) getDuraFromType(type));
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(Util.color("&f" + type.toString().toLowerCase().replaceAll("_", " ")));
            item.setItemMeta(meta);
            applyNBTTag(item, "type", type.toString());
            this.inventory.addItem(item);

        }
        player.openInventory(this.inventory);
    }


    @Override
    public void onClick(InventoryClickEvent event) {
        if(event.getCurrentItem() == null) return;
        if(event.getCurrentItem().getType().equals(Material.AIR)) return;
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        Pet pet = Duckypets.getOwnerPets().get(player);
        String type = NBTEditor.getString(item, "type");
        Ocelot.Type cat = Ocelot.Type.valueOf(type);
        pet.setCatType(cat);
        player.sendMessage(Util.color("&aJe hebt je cat type veranderd naar " + type.toLowerCase().replaceAll("_", " ")));
        player.closeInventory();
    }

    private int getDuraFromType(Ocelot.Type type) {
        String typeString = type.toString().toUpperCase();

        switch (typeString) {
            case "WILD_OCELOT":
                return 4;
            case "BLACK_CAT":
                return 15;
            case "RED_CAT":
                return 14;
            case "SIAMESE_CAT":
                return 8;
        }
        return 0;

    }

    public void applyNBTTag(ItemStack itemStack, String key, Object value) {
        ItemStack is = NBTEditor.set(itemStack, value, key);
        ItemMeta itemMeta = is.getItemMeta();
        itemStack.setItemMeta(itemMeta);
    }
}
