package dev.thebathduck.duckypets.listeners;

import com.sun.org.apache.bcel.internal.generic.PUSH;
import dev.thebathduck.duckypets.Duckypets;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;

public class PetWorldInteract implements Listener {


    @EventHandler
    public void onSheepEat(EntityChangeBlockEvent event) {
        if(event.getEntity() instanceof Sheep) {
            if(Duckypets.getEntityPets().get(event.getEntity()) != null) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onSheepShear(PlayerShearEntityEvent event) {
        if(event.getEntity() instanceof Sheep) {
            if(Duckypets.getEntityPets().get(event.getEntity()) != null) {
                event.setCancelled(true);
            }
        }
    }

}
