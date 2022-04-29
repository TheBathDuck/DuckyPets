package dev.thebathduck.duckypets.menu;

import dev.thebathduck.duckypets.Duckypets;
import dev.thebathduck.duckypets.objects.Pet;
import dev.thebathduck.duckypets.utils.GUIHolder;
import dev.thebathduck.duckypets.utils.ItemBuilder;
import dev.thebathduck.duckypets.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Wolf;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PetEditor extends GUIHolder {
    public PetEditor(){}

    public void open(Player player, Pet pet) {
        this.inventory = Bukkit.createInventory(this, 3*9, Util.color("Pet Bewerken | " + pet.getName()));

        if(Duckypets.getInstance().getConfig().getBoolean("can-player-rename")) {
            this.inventory.setItem(10, new ItemBuilder(Material.NAME_TAG).setColoredName("&6Verander Naam")
                    .addLoreLine("")
                    .addLoreLine("&7Geef je pet een andere")
                    .addLoreLine("&7naam.")
                    .build()
            );
        } else {
            this.inventory.setItem(10, new ItemBuilder(Material.BARRIER).setColoredName("&6Verander Naam")
                    .addLoreLine("")
                    .addLoreLine("&7Jij kan de naam niet aanpassen")
                    .addLoreLine("&7van pets.")
                    .build()
            );
        }

        if(pet.getEntity() instanceof Wolf) {
            this.inventory.setItem(11, new ItemBuilder(Material.BONE).setColoredName("&6Halsband kleur")
                    .addLoreLine("")
                    .addLoreLine("&7Verander de kleur van")
                    .addLoreLine("&7de halsband.")
                    .build()
            );
        }
        if(pet.getEntity() instanceof Sheep) {
            this.inventory.setItem(11, new ItemBuilder(Material.WOOL, 1).setColoredName("&6Schaap kleur")
                    .setDurability((short) 0)
                    .addLoreLine("")
                    .addLoreLine("&7Verander de kleur van")
                    .addLoreLine("&7de schaap.")
                    .build()
            );
        }
        if(pet.getEntity() instanceof Ocelot) {
            this.inventory.setItem(11, new ItemBuilder(Material.RAW_FISH, 1).setColoredName("&6Kat type")
                    .addLoreLine("")
                    .addLoreLine("&7Verander het type van")
                    .addLoreLine("&7je kat.")
                    .build()
            );
        }


        this.inventory.setItem(16, new ItemBuilder(Material.PURPUR_BLOCK).setColoredName("&6Stop terug")
                .addLoreLine("")
                .addLoreLine("&7Stop je pet terug in de")
                .addLoreLine("&7dierenmand.")
                .build()
        );

        player.openInventory(this.inventory);
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if(event.getCurrentItem() == null) return;
        if(event.getCurrentItem().getType().equals(Material.AIR)) return;
        ItemStack item = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();
        Pet pet = Duckypets.getOwnerPets().get(player);
        event.setCancelled(true);

        if(item.getType().equals(Material.NAME_TAG)) {
            Duckypets.getEventName().add(player);
            player.closeInventory();
            player.sendMessage(Util.color("&aType de naam in die je wilt hebben voor je pet in de chat."));
        }

        if(item.getType().equals(Material.PURPUR_BLOCK)) {
            pet.remove();
            player.closeInventory();
            return;
        }

        if(item.getType().equals(Material.RAW_FISH)) {
            Duckypets.getPetCatMenu().open(player, pet);
            return;
        }

        if(item.getType().equals(Material.BONE) || item.getType().equals(Material.WOOL)) {
            Duckypets.getPetCollarMenu().open(player, pet);
            return;
        }
    }
}
