package dev.thebathduck.duckypets;

import dev.thebathduck.duckypets.commands.PetCommand;
import dev.thebathduck.duckypets.listeners.*;
import dev.thebathduck.duckypets.listeners.chatevents.PetNameEvent;
import dev.thebathduck.duckypets.listeners.chatevents.WorldListener;
import dev.thebathduck.duckypets.menu.PetEditor;
import dev.thebathduck.duckypets.menu.PetMenu;
import dev.thebathduck.duckypets.menu.editmenu.PetCatMenu;
import dev.thebathduck.duckypets.menu.editmenu.PetCollarMenu;
import dev.thebathduck.duckypets.objects.Pet;
import dev.thebathduck.duckypets.tasks.PetTask;
import dev.thebathduck.duckypets.utils.ConfigurationFile;
import dev.thebathduck.duckypets.utils.GUIHolder;
import dev.thebathduck.duckypets.utils.Util;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

public final class Duckypets extends JavaPlugin {

    private static @Getter Duckypets instance;
    private static @Getter ConfigurationFile data;
    private static @Getter PetMenu petMenu;
    private static @Getter PetEditor petEditor;
    private static @Getter PetCollarMenu petCollarMenu;
    private static @Getter PetCatMenu petCatMenu;

    private static @Getter HashMap<Player, Pet> ownerPets = new HashMap<>();
    private static @Getter HashMap<Entity, Pet> entityPets = new HashMap<>();
    private static @Getter ArrayList<Pet> pets = new ArrayList<>();

    private static @Getter ArrayList<Player> eventName = new ArrayList<>();

    @Override
    public void onEnable() {
        instance = this;
        GUIHolder.init(this);
        data = new ConfigurationFile(this, "data.yml", true);
        petMenu = new PetMenu();
        petEditor = new PetEditor();
        petCollarMenu = new PetCollarMenu();
        petCatMenu = new PetCatMenu();
        getCommand("pet").setExecutor(new PetCommand());
        Bukkit.getPluginManager().registerEvents(new BlockListener(), this);
        Bukkit.getPluginManager().registerEvents(new PetDamageListener(), this);
        Bukkit.getPluginManager().registerEvents(new PetInteractListener(), this);
        Bukkit.getPluginManager().registerEvents(new PetNameEvent(), this);
        Bukkit.getPluginManager().registerEvents(new PetWorldInteract(), this);
        Bukkit.getPluginManager().registerEvents(new QuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new VoucherListener(), this);
        Bukkit.getPluginManager().registerEvents(new WorldListener(), this);
        (new PetTask()).runTaskTimer(this, 0L, 5L);

    }

    @Override
    public void onDisable() {
        for(Pet pet : pets) {
            pet.getPlayer().sendMessage(Util.color("&cDe pet plugin werdt herladen, je pet is gedespawned."));
            pet.remove();
        }
    }

    public void despawnPet(Pet pet) {
        Player owner = pet.getPlayer();
        Entity entity = pet.getEntity();

        ownerPets.remove(owner);
        entityPets.remove(entity);
        pets.remove(pet);

        pet.remove();
        pet = null;

    }

}
