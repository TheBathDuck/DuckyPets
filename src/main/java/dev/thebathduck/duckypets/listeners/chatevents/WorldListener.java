package dev.thebathduck.duckypets.listeners.chatevents;

import dev.thebathduck.duckypets.Duckypets;
import dev.thebathduck.duckypets.objects.Pet;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class WorldListener implements Listener {
    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Pet pet = Duckypets.getOwnerPets().get(event.getPlayer());
        Player player = event.getPlayer();
        if(pet != null) {
            pet.getEntity().teleport(player.getLocation());
        }


    }
}
