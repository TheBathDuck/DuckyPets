package dev.thebathduck.duckypets.commands;

import dev.thebathduck.duckypets.Duckypets;
import dev.thebathduck.duckypets.objects.Pet;
import dev.thebathduck.duckypets.utils.ConfigurationFile;
import dev.thebathduck.duckypets.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.Console;
import java.util.Locale;
import java.util.Random;

public class PetCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //if(!(sender instanceof Player)) return false;
        //Player player = (Player) sender;
        if(sender instanceof Player && !sender.hasPermission("duckypets.manage")) {
            sender.sendMessage(Util.color("&cJij hebt hier geen permissies voor!"));
            return false;
        }

        // - /pet create <Owner> <EntityType> <Name>
        if(args.length == 4 && args[0].equals("create")) {
            Player target = Bukkit.getPlayer(args[1]);
            if(target == null) {
                sender.sendMessage(Util.color("&4" + args[1] + " &cis niet online"));
                return false;
            }
            try {
                EntityType type = EntityType.valueOf(args[2].toUpperCase());
            } catch (IllegalArgumentException e) {
                sender.sendMessage(Util.color("&cJe hebt geen geldige mob opgegeven!"));
                return false;
            }
            EntityType type = EntityType.valueOf(args[2].toUpperCase());
            String name = args[3];
            if(!(name.length() > 2 && name.length() < 16)) {
                sender.sendMessage(Util.color("&cDe naam moet minimaal 3 letters en maximaal 16 letters zijn!"));
                return false;
            }
            int petID = new Random().nextInt(500000);
            FileConfiguration data = Duckypets.getData().getConfig();
            data.set("pets." + target.getUniqueId().toString() + "." + petID + ".type", type.toString().toUpperCase());
            data.set("pets." + target.getUniqueId().toString() + "." + petID + ".name", name);
            data.set("pets." + target.getUniqueId().toString() + "." + petID + ".collar", "GRAY");
            data.set("pets." + target.getUniqueId().toString() + "." + petID + ".cattype", "RED_CAT");
            data.set("pets." + target.getUniqueId().toString() + "." + petID + ".woolcolor", "GRAY");
            Duckypets.getData().saveConfig();

            sender.sendMessage(Util.color("&aPet succesvol aangemaakt en toegevoegd aan &2" + target.getName() + "'s &adierenmand."));
            target.sendMessage(Util.color("&aEr is een &2" + args[2].toLowerCase() + " &atoegevoegd aan je dierenmand!"));
            return false;
        }

        if(args.length == 2 && args[0].equals("list")) {
            Player target = Bukkit.getPlayer(args[1]);
            if(target == null) {
                sender.sendMessage(Util.color("&cDeze speler is niet online!"));
                return false;
            }
            if(countPets(target.getPlayer()) == 0) {
                sender.sendMessage(Util.color("&cDeze speler heeft geen pets."));
                return false;
            }
            FileConfiguration data = Duckypets.getData().getConfig();
            sender.sendMessage(Util.color("&6" + target.getName() + "&e's pets."));
            for(String petID : data.getConfigurationSection("pets." + target.getUniqueId().toString()).getKeys(false)) {
                String name = data.getString("pets." + target.getUniqueId().toString() + "." + petID + ".name");
                String type = data.getString("pets." + target.getUniqueId().toString() + "." + petID + ".type");
                sender.sendMessage(Util.color(" &7- &6Type: &e" + type.toUpperCase() + "&6, Naam: &e" + name + " &7(ID: " + petID + "&7)"));
            }
            sender.sendMessage(Util.color(""));
            return false;
        }

        if(args.length == 3 && args[0].equals("remove")) {
            Player target = Bukkit.getPlayer(args[1]);
            if(target == null) {
                sender.sendMessage(Util.color("&cDeze speler is niet online!"));
                return false;
            }
            FileConfiguration data = Duckypets.getData().getConfig();

            String petID = args[2];
            if(!isIdValid(target, petID, data)) {
                sender.sendMessage(Util.color("&cJe hebt geen geldig ID opgegeven!"));
                return false;
            }

            Pet targetPet = Duckypets.getOwnerPets().get(target);
            if(targetPet != null) {
                targetPet.remove();
            }

            data.set("pets." + target.getUniqueId().toString() + "." + petID, null);
            sender.sendMessage(Util.color("&2Pet van &a" + target.getName() + " &2verwijderd."));
            Duckypets.getData().saveConfig();
            return false;
        }

        if(args.length == 2 && args[0].equalsIgnoreCase("removeall")) {
            Player target = Bukkit.getPlayer(args[1]);
            if(target == null) {
                sender.sendMessage(Util.color("&cDeze speler is niet online!"));
                return false;
            }
            Pet targetPet = Duckypets.getOwnerPets().get(target);
            if(targetPet != null) {
                targetPet.remove();
            }
            FileConfiguration data = Duckypets.getData().getConfig();
            data.set("pets." + target.getUniqueId().toString(), null);
            sender.sendMessage(Util.color("&2Je hebt alle dieren uit &a" + target.getName() + "'s &2dieren uit zijn dierenmand verwijderd."));
            return false;
        }

        // - /pet voucher MOB NAAM;
        if(args.length == 4 && args[0].equalsIgnoreCase("voucher")) {
            Player target = Bukkit.getPlayer(args[1]);
            if(target == null) {
                sender.sendMessage(Util.color("&cDeze speler is niet online!"));
                return false;
            }
            try {
                EntityType type = EntityType.valueOf(args[2].toUpperCase());
            } catch (IllegalArgumentException e) {
                sender.sendMessage(Util.color("&cJe hebt geen geldige mob opgegeven!"));
                return false;
            }
            String mob = args[2].toLowerCase();
            String name = args[3];
            ItemStack voucher = Util.createVoucher(mob, name, target.getName());
            target.getInventory().addItem(voucher);
            target.sendMessage(Util.color("&aJe hebt een pet voucher voor een &2" + mob + " &aontvangen!"));
            sender.sendMessage(Util.color("&aJe hebt een voucher aangemaakt voor de mob &2" + mob + " &amet de naam &2" + name + "&a."));
            return false;
        }

        sendHelp(sender);

        return false;
    }

    public void sendHelp(CommandSender player ){
        player.sendMessage(Util.color(""));
        player.sendMessage(Util.color("&2/pet &acreate <Speler> <Entity> <Naam> &7- &aMaak een pet aan!"));
        player.sendMessage(Util.color("&2/pet &alist <Speler> &7- &aBekijk een speler zijn pet's!"));
        player.sendMessage(Util.color("&2/pet &aremove <Speler> <ID> &7- &aVerwijder pets van een speler."));
        player.sendMessage(Util.color("&2/pet &aremoveall <Speler> &a- &aVerwijder alle pets van een speler."));
        player.sendMessage(Util.color("&2/pet &avoucher <Speler> <Mob> <Naam> &a- &aGeef een voucher aan een speler."));
        player.sendMessage(Util.color(""));
    }

    public boolean isIdValid(Player player, String id, FileConfiguration data) {
        if(data.getString("pets." + player.getUniqueId().toString() + "." + id + ".type") == null) {
            return false;
        }
        return true;
    }
    public int countPets(Player player) {
        FileConfiguration data = Duckypets.getData().getConfig();
        int pets = 0;

        try {
            for(String petID : data.getConfigurationSection("pets." + player.getUniqueId().toString()).getKeys(false)) {
                pets++;
            }
        } catch (NullPointerException e) {
            return 0;
        }

        return pets;
    }
}
