package dev.thebathduck.duckypets.listeners.chatevents;

import dev.thebathduck.duckypets.Duckypets;
import dev.thebathduck.duckypets.objects.Pet;
import dev.thebathduck.duckypets.utils.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PetNameEvent implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if(!(Duckypets.getEventName().contains(player))) return;
        Duckypets.getEventName().remove(player);
        String message = event.getMessage();
        if(!(message.length() > 3 && message.length() < 16)) {
            player.sendMessage(Util.color("&cJe pet naam mag minimaal 4 letters en maximaal 16 letters hebben."));
            return;
        }
        Pet pet = Duckypets.getOwnerPets().get(player);
        pet.setName(Util.color(message));
    }
}
