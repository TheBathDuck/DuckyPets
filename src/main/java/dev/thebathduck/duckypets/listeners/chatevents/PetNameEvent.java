package dev.thebathduck.duckypets.listeners.chatevents;

import dev.thebathduck.duckypets.Duckypets;
import dev.thebathduck.duckypets.objects.Pet;
import dev.thebathduck.duckypets.utils.Util;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;

public class PetNameEvent implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if(!(Duckypets.getEventName().contains(player))) return;
        Duckypets.getEventName().remove(player);
        event.setCancelled(true);
        String message = event.getMessage();

        FileConfiguration config = Duckypets.getInstance().getConfig();


        int min = config.getInt("name.minimum-length");
        int max = config.getInt("name.maximum-length");

        if(!(message.length() >= min && message.length() < max)) {
            player.sendMessage(Util.color("&cJe pet naam moet minimaal " + min + " letters en maximaal " + max + " letters hebben."));
            return;
        }

        List<String> list = config.getStringList("name.blacklist");

        for(String blacklistedName : list) {
            if(message.toLowerCase().contains(blacklistedName.toLowerCase())) {
                player.sendMessage(Util.color("&cEr staan woorden in deze naam die niet gebruikt mogen worden!"));
                return;
            }
        }


        Pet pet = Duckypets.getOwnerPets().get(player);

        if(config.getBoolean("name.allow-colors")) {
            pet.setName(message);
        }else{
            pet.setName(message.replaceAll("&", ""));
        }
    }
}
