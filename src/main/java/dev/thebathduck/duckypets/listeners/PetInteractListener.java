package dev.thebathduck.duckypets.listeners;

import dev.thebathduck.duckypets.Duckypets;
import dev.thebathduck.duckypets.objects.Pet;
import dev.thebathduck.duckypets.utils.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PetInteractListener implements Listener {

    @EventHandler
    public void onPetListener(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if(event.getRightClicked() == null) return;
        Pet pet = Duckypets.getOwnerPets().get(player);
        if(pet == null) return;
        if(!event.getRightClicked().equals(pet.getEntity())) return;
        if(!player.isSneaking()) return;
        Duckypets.getPetEditor().open(player, pet);


    }
}
