package dev.thebathduck.duckypets.listeners;

import dev.thebathduck.duckypets.Duckypets;
import dev.thebathduck.duckypets.objects.Pet;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Pet pet = Duckypets.getOwnerPets().get(player);
        if(pet != null) {
            Duckypets.getInstance().despawnPet(pet);
        }




    }
}
