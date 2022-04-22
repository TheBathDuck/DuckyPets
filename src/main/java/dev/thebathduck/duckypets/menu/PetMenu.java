package dev.thebathduck.duckypets.menu;

import dev.thebathduck.duckypets.Duckypets;
import dev.thebathduck.duckypets.objects.Pet;
import dev.thebathduck.duckypets.utils.GUIHolder;
import dev.thebathduck.duckypets.utils.ItemBuilder;
import dev.thebathduck.duckypets.utils.Util;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PetMenu extends GUIHolder {
    public PetMenu(){}

    public void open(Player player) {
        FileConfiguration data = Duckypets.getData().getConfig();
        this.inventory = Bukkit.createInventory(this, 6*9, Util.color("Dierenmand"));
        drawUI();


        if(countPets(player) != 0) {
            for(String petID : data.getConfigurationSection("pets." + player.getUniqueId().toString()).getKeys(false)) {
                EntityType type = EntityType.valueOf(data.getString("pets." + player.getUniqueId().toString() + "." + petID + ".type"));
                String name = data.getString("pets." + player.getUniqueId().toString() + "." + petID + ".name");

                ItemStack item = new ItemBuilder(Material.MONSTER_EGG)
                        .setMonsterType(type)
                        .setColoredName("&f" + name)
                        .addLoreLine("")
                        .addLoreLine("&7Klik hier om te spawnen!")
                        .setNBT("type", type.toString().toUpperCase())
                        .setNBT("name", name)
                        .setNBT("petId", petID)
                        .build();

                this.inventory.addItem(item);
            }
        }

        player.openInventory(this.inventory);
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if(event.getCurrentItem() == null) return;
        if(event.getCurrentItem().getType().equals(Material.AIR)) return;
        event.setCancelled(true);
        if(event.getCurrentItem().getType().equals(Material.STAINED_GLASS_PANE)) return;
        ItemStack item = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        if(item.getType().equals(Material.BARRIER)) {
            Pet despawnPet = Duckypets.getOwnerPets().get(player);
            if(despawnPet != null) {
                Duckypets.getInstance().despawnPet(despawnPet);
                player.sendMessage(Util.color("&cJe hebt je pet gedespawned!"));
                return;
            }
            player.sendMessage(Util.color("&cJe hebt geen pet om te despawnen."));
            return;
        }
        Pet current = Duckypets.getOwnerPets().get(player);
        if(current != null) {
            Duckypets.getInstance().despawnPet(current);
        }

        EntityType type = EntityType.valueOf(NBTEditor.getString(item, "type").toUpperCase());
        String name = Util.color(NBTEditor.getString(item, "name"));
        String petId = NBTEditor.getString(item, "petId");

        Pet pet = new Pet(player, type, name, petId);
        Duckypets.getPets().add(pet);
        Duckypets.getEntityPets().put(pet.getEntity(), pet);
        Duckypets.getOwnerPets().put(pet.getPlayer(), pet);

        player.sendMessage(Util.color("&2Je hebt &a" + ChatColor.stripColor(pet.getName()) + " &2gespawned."));
        player.closeInventory();
    }
    public void drawUI() {
        ItemStack empty = new ItemBuilder(Material.STAINED_GLASS_PANE, 1).setDurability((short) 0).setColoredName(" ").build();
        int slot = 45;
        for(int i = 0; i < 9; i++) {
            this.inventory.setItem(slot, empty);
            slot++;
        }

        this.inventory.setItem(49, new ItemBuilder(Material.BARRIER).setColoredName("&cDespawn je pet.").build());

    }

    public int countPets(Player player) {
        FileConfiguration data = Duckypets.getData().getConfig();
        int pets = 0;

        try {
            for(String petID : data.getConfigurationSection("pets." + player.getUniqueId().toString()).getKeys(false)) {
                pets++;
            }
        } catch (NullPointerException e) {
            return 0;
        }

        return pets;
    }
}
