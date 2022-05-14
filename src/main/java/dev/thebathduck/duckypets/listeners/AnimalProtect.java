package dev.thebathduck.duckypets.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityTameEvent;

public class AnimalProtect implements Listener {
    @EventHandler
    public void onAnimalMate(CreatureSpawnEvent event) {
        if(event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.BREEDING) {
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onAnimalTame(EntityTameEvent event) {
        event.setCancelled(true);
    }
}
