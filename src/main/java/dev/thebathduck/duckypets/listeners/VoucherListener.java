package dev.thebathduck.duckypets.listeners;

import dev.thebathduck.duckypets.Duckypets;
import dev.thebathduck.duckypets.utils.Util;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class VoucherListener implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.getItem() == null) return;
        if(!(event.getHand().equals(EquipmentSlot.HAND))) return;
        ItemStack item = event.getItem();
        if(!(item.getType().equals(Material.PAPER))) return;
        String petType = NBTEditor.getString(item, "mob");
        String petName = NBTEditor.getString(item, "name");
        if(petType == null || petName == null) return;
        Player player = event.getPlayer();
        int petID = new Random().nextInt(500000);
        FileConfiguration data = Duckypets.getData().getConfig();
        data.set("pets." + player.getUniqueId().toString() + "." + petID + ".type", petType.toString().toUpperCase());
        data.set("pets." + player.getUniqueId().toString() + "." + petID + ".name", Util.color(petName));
        data.set("pets." + player.getUniqueId().toString() + "." + petID + ".collar", "GRAY");
        data.set("pets." + player.getUniqueId().toString() + "." + petID + ".cattype", "RED_CAT");
        data.set("pets." + player.getUniqueId().toString() + "." + petID + ".woolcolor", "GRAY");
        Duckypets.getData().saveConfig();
        player.sendMessage(Util.color("&aJe hebt een voucher geclaimed voor een &2" + petType.toLowerCase() + " &apet."));

        if(item.getAmount() == 1) {
            item.setAmount(0);
        } else {
            item.setAmount(item.getAmount() -1);
        }

    }
}
