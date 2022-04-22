package dev.thebathduck.duckypets.listeners;

import dev.thebathduck.duckypets.Duckypets;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class BlockListener implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(!(event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) return;
        if(event.getClickedBlock() == null) return;
        if(!(event.getHand().equals(EquipmentSlot.HAND))) return;
        if(event.getClickedBlock().getType().equals(Material.PURPUR_BLOCK)) {
            Duckypets.getPetMenu().open(event.getPlayer());


        }
    }
}
