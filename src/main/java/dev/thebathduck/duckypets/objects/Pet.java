package dev.thebathduck.duckypets.objects;

import dev.thebathduck.duckypets.Duckypets;
import dev.thebathduck.duckypets.utils.Util;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.DyeColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Locale;

public class Pet {

    private Player player;
    private EntityType entityType;
    private Entity entity;
    private String name;
    private String petID;
    private Duckypets plugin;

    public Pet(Player player, EntityType type, String name, String petID) {
        this.plugin = Duckypets.getInstance();
        this.player = player;
        this.entityType = type;
        this.name = Util.color(name);
        this.entity = player.getWorld().spawnEntity(player.getLocation(), type);
        this.entity.setInvulnerable(true);
        this.entity.setCustomNameVisible(true);
        this.entity.setCustomName(name);
        this.entity.setInvulnerable(true);
        this.petID = petID;

        if(this.entity instanceof Wolf) {
            String dyeColor = Duckypets.getData().getConfig().getString("pets." + player.getUniqueId().toString() + "." + petID + ".collar");
            Wolf wolf = (Wolf) this.entity;
            wolf.setOwner(null);
            wolf.setSitting(false);
            wolf.setTamed(true);
            wolf.setCollarColor(DyeColor.valueOf(dyeColor));
        }
        if(this.entity instanceof Ocelot) {
            String catType = Duckypets.getData().getConfig().getString("pets." + player.getUniqueId().toString() + "." + petID + ".cattype");
            Ocelot kat = (Ocelot) this.entity;
            kat.setOwner(null);
            kat.setSitting(false);
            kat.setCatType(Ocelot.Type.valueOf(catType));
        }
        if(this.entity instanceof Sheep) {
            Sheep sheep = (Sheep) this.entity;
            String color = Duckypets.getData().getConfig().getString("pets." + player.getUniqueId().toString() + "." + petID + ".woolcolor");
            DyeColor dyeColor = DyeColor.valueOf(color);
            sheep.setColor(dyeColor);
        }

    }

    public Entity getEntity() {
        return this.entity;
    }
    public Player getPlayer() {
        return this.player;
    }
    public EntityType getEntityType() {
        return this.entityType;
    }
    public String getName() {
        return this.name;
    }

    public String getPetID() {
        return this.petID;
    }

    public void remove() {
        Duckypets.getOwnerPets().remove(player);
        Duckypets.getEntityPets().remove(entity);
        Duckypets.getPets().remove(this);
        this.entity.remove();
    }

    public DyeColor getDogCollar() {
        if(!(this.entity instanceof Wolf)) return null;
        Wolf wolf = (Wolf) this.entity;
        return wolf.getCollarColor();
    }

    public Ocelot.Type getCatType() {
        if(!(this.entity instanceof Ocelot)) return null;
        Ocelot cat = (Ocelot) this.entity;
        return cat.getCatType();
    }

    public void setCatType(Ocelot.Type type) {
        if(!(this.entity instanceof Ocelot)) return;
        Ocelot cat = (Ocelot) this.entity;
        cat.setCatType(type);
        Duckypets.getData().getConfig().set("pets." + player.getUniqueId().toString() + "." + petID + ".cattype", type.toString().toUpperCase());
        Duckypets.getData().saveConfig();
    }

    public void setColarColor(DyeColor color) {
        if(!(this.entity instanceof Wolf)) return;
        Wolf wolf = (Wolf) this.entity;
        wolf.setCollarColor(color);
        Duckypets.getData().getConfig().set("pets." + player.getUniqueId().toString() + "." + petID + ".collar", color.toString().toUpperCase());
        Duckypets.getData().saveConfig();
    }

    public void setName(String name) {
        this.name = Util.color(name);
        this.entity.setCustomName(Util.color(name));
        Duckypets.getData().getConfig().set("pets." + player.getUniqueId().toString() + "." + petID + ".name", Util.color(name));
        Duckypets.getData().saveConfig();
    }

    public void setSheepColor(DyeColor color) {
        if(!(this.entity instanceof Sheep)) return;
        Sheep sheep = (Sheep) this.entity;
        sheep.setColor(color);
        Duckypets.getData().getConfig().set("pets." + player.getUniqueId().toString() + "." + petID + ".woolcolor", color.toString().toUpperCase());
        Duckypets.getData().saveConfig();
    }

    public void setNBT(String key, String value) {
        NBTEditor.set(this.entity, key, value);
    }




}
