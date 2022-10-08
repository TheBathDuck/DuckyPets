package dev.thebathduck.duckypets.commands;

import dev.thebathduck.duckypets.Duckypets;
import dev.thebathduck.duckypets.objects.Pet;
import dev.thebathduck.duckypets.utils.Util;
import dev.thebathduck.duckypets.utils.command.AbstractCommand;
import dev.thebathduck.duckypets.utils.command.Argument;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PetCommand extends AbstractCommand {
    public PetCommand() {
        super("pet",
                new Argument("create", "create <Speler> <Entity> <Naam>", "Maak een pet aan!", "duckypets.create"),
                new Argument("list", "list <Speler>", "Bekijk een speler zijn pet's!", "duckypets.list"),
                new Argument("remove", "remove <Speler> <ID>", "Verwijder pets van een speler!", "duckypets.remove"),
                new Argument("removeall", "removeall <Speler>", "Verwijder alle pets van een speler!", "duckypets.removeall"),
                new Argument("voucher", "voucher <Entity> <Naam>", "Maak een voucher aan!", "duckypets.voucher"),
                new Argument("menu", "menu", "Open het menu waar je maar wilt!", "duckypets.menu")
        );
    }

    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {

        if(args.length == 0) {
            sendNotEnoughArguments(this);
            return;
        }

        switch (args[0].toLowerCase()) {
            case "create": {
                if(args.length < 4) {
                    sender.sendMessage(Util.color("&cGebruik: /pet create <Speler> <Entity> <Naam>"));
                    return;
                }
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                if(target == null || !target.hasPlayedBefore()) {
                    sender.sendMessage(Util.color("&4" + args[1] + " &cheeft niet op deze server gespeeld!"));
                    return;
                }
                try {
                    EntityType type = EntityType.valueOf(args[2].toUpperCase());
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(Util.color("&cJe hebt geen geldige mob opgegeven!"));
                    return;
                }
                if(!isAllowed(EntityType.valueOf(args[2].toUpperCase()))) {
                    sender.sendMessage(Util.color("&cDeze mob is niet toegestaan."));
                    return;
                }
                EntityType type = EntityType.valueOf(args[2].toUpperCase());
                String name = args[3];
                if(!(name.length() > 2 && name.length() < 16)) {
                    sender.sendMessage(Util.color("&cDe naam moet minimaal 3 letters en maximaal 16 letters zijn!"));
                    return;
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
                if(target.getPlayer() != null) {
                    target.getPlayer().sendMessage(Util.color("&aEr is een &2" + args[2].toLowerCase() + " &atoegevoegd aan je dierenmand!"));
                }
            }
            break;
            case "list": {
                if(args.length < 2) {
                    sender.sendMessage(Util.color("&cGebruik: /pet list <Speler>"));
                    return;
                }
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                if(target == null || !target.hasPlayedBefore()) {
                    sender.sendMessage(Util.color("&4" + args[1] + " &cheeft niet op deze server gespeeld!"));
                    return;
                }
                if(countPets(target) == 0) {
                    sender.sendMessage(Util.color("&cDeze speler heeft geen pets."));
                    return;
                }
                FileConfiguration data = Duckypets.getData().getConfig();
                sender.sendMessage(Util.color("&6" + target.getName() + "&e's pets."));
                for(String petID : data.getConfigurationSection("pets." + target.getUniqueId().toString()).getKeys(false)) {
                    String name = data.getString("pets." + target.getUniqueId().toString() + "." + petID + ".name");
                    String type = data.getString("pets." + target.getUniqueId().toString() + "." + petID + ".type");
                    sender.sendMessage(Util.color(" &7- &6Type: &e" + type.toUpperCase() + "&6, Naam: &e" + name + " &7(ID: " + petID + "&7)"));
                }
                sender.sendMessage(Util.color(""));
            }
            break;
            case "remove": {
                if(args.length < 3) {
                    sender.sendMessage(Util.color("&cGebruik: /pet remove <Speler> <ID>"));
                    return;
                }
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                if(target == null || !target.hasPlayedBefore()) {
                    sender.sendMessage(Util.color("&4" + args[1] + " &cheeft niet op deze server gespeeld!"));
                    return;
                }
                FileConfiguration data = Duckypets.getData().getConfig();

                String petID = args[2];
                if(!isIdValid(target, petID, data)) {
                    sender.sendMessage(Util.color("&cJe hebt geen geldig ID opgegeven!"));
                    return;
                }

                if(Duckypets.getOwnerPets().containsKey(target.getPlayer())) {
                    Pet targetPet = Duckypets.getOwnerPets().get(target.getPlayer());
                    if (targetPet != null) {
                        targetPet.remove();
                    }
                }

                data.set("pets." + target.getUniqueId().toString() + "." + petID, null);
                sender.sendMessage(Util.color("&2Pet van &a" + target.getName() + " &2verwijderd."));
                Duckypets.getData().saveConfig();
            }
            break;
            case "removeall": {
                if(args.length < 2) {
                    sender.sendMessage(Util.color("&cGebruik: /pet removeall <Speler>"));
                    return;
                }
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                if(target == null || !target.hasPlayedBefore()) {
                    sender.sendMessage(Util.color("&4" + args[1] + " &cheeft niet op deze server gespeeld!"));
                    return;
                }
                if(Duckypets.getOwnerPets().containsKey(target.getPlayer())) {
                    Pet targetPet = Duckypets.getOwnerPets().get(target.getPlayer());
                    if (targetPet != null) {
                        targetPet.remove();
                    }
                }
                FileConfiguration data = Duckypets.getData().getConfig();
                data.set("pets." + target.getUniqueId().toString(), null);
                sender.sendMessage(Util.color("&2Je hebt alle dieren uit &a" + target.getName() + "'s &2dierenmand verwijderd."));
            }
            break;
            case "voucher": {
                if(!(sender instanceof Player)) {
                    sender.sendMessage(Util.color("&cJe moet een speler zijn om dit commando uit te voeren!"));
                    return;
                }
                if(args.length < 3) {
                    sender.sendMessage(Util.color("&cGebruik: /pet voucher <Entity> <Naam>"));
                    return;
                }
                try {
                    EntityType.valueOf(args[1].toUpperCase());
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(Util.color("&cJe hebt geen geldige mob opgegeven!"));
                    return;
                }

                if(!isAllowed(EntityType.valueOf(args[1].toUpperCase()))) {
                    sender.sendMessage(Util.color("&cDeze mob is niet toegestaan."));
                    return;
                }

                String mob = args[1].toLowerCase();
                String name = args[2];
                ItemStack voucher = Util.createVoucher(mob, name);
                ((Player) sender).getInventory().addItem(voucher);
                sender.sendMessage(Util.color("&aJe hebt een voucher aangemaakt voor de entity &2" + mob + " &amet de naam &2" + name + "&a."));
            }
            break;
            case "menu": {
                if(!(sender instanceof Player)) {
                    sender.sendMessage(Util.color("&cJe moet een speler zijn om dit commando uit te voeren!"));
                    return;
                }
                Duckypets.getPetMenu().open((Player) sender);
            }
            break;

            default: {
                sendNotEnoughArguments(this);
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        ArrayList<String> visibleArguments = new ArrayList<>();
        for(Argument argument : arguments) {
            if (argument.getPermission() == null) {
                visibleArguments.add(argument.getArgumentsRaw());
                continue;
            }
            if(commandSender.hasPermission(argument.getPermission())) {
                visibleArguments.add(argument.getArgumentsRaw());
            }
        }
        if (args.length == 1) {
            return getApplicableTabCompleters(args[0], visibleArguments);
        }
        return Collections.emptyList();
    }

    public boolean isIdValid(OfflinePlayer player, String id, FileConfiguration data) {
        System.out.print(player.getUniqueId());
        if(data.getString("pets." + player.getUniqueId().toString() + "." + id + ".type") == null) {
            return false;
        }
        return true;
    }
    public int countPets(OfflinePlayer player) {
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

    public boolean isAllowed(EntityType entityType) {
        String entityString = entityType.toString().toUpperCase();
        if(!Duckypets.getAllowed().contains(entityString)) return false;
        return true;
    }
}
