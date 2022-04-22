package dev.thebathduck.duckypets.tasks;

import dev.thebathduck.duckypets.Duckypets;
import dev.thebathduck.duckypets.objects.Pet;
import net.minecraft.server.v1_12_R1.EntityCreature;
import net.minecraft.server.v1_12_R1.EntityInsentient;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Sittable;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Locale;

public class PetTask extends BukkitRunnable {



    @Override
    public void run() {
        for(Pet pet : Duckypets.getPets()) {
            if(pet.getEntity() != null && !pet.getEntity().isDead()) {
                double distance = pet.getEntity().getLocation().distanceSquared(pet.getPlayer().getLocation());
                Entity entity = pet.getEntity();
                if(entity == null) return;
                if(distance > 4.0D) {
                    if(distance  > 100.0D) {
                        entity.teleport(pet.getPlayer());
                    } else {
                        moveTo((LivingEntity) entity, pet.getPlayer().getLocation(), 1.9F);
                    }
                }
                if(pet.getEntity() instanceof Sittable) {
                    Sittable sittable = (Sittable) pet.getEntity();
                    sittable.setSitting(false);
                }
            }
        }
    }
    // - Old method.
//    public void walkTo(Location location, double speed, LivingEntity entity) {
//        EntityInsentient c = (EntityInsentient)((CraftLivingEntity)entity).getHandle();
//        c.getNavigation().a(location.getX(), location.getY(), location.getZ(), speed);
//    }

    public void moveTo(LivingEntity entity, Location moveTo, float speed)
    {
        CraftCreature creature = (CraftCreature) entity;
        EntityCreature cr = creature.getHandle();
        cr.getNavigation().a(moveTo.getX(), moveTo.getY(), moveTo.getZ(), speed);
    }
}
