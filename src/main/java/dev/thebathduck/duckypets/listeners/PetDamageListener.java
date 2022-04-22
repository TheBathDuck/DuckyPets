package dev.thebathduck.duckypets.listeners;

import dev.thebathduck.duckypets.Duckypets;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PetDamageListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if(Duckypets.getEntityPets().get(event.getEntity()) != null) {
            event.setCancelled(true);
        }
    }


}
