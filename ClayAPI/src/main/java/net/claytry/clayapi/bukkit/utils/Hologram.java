package net.claytry.clayapi.bukkit.utils;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rexlManu on 22.07.2017.
 * Plugin by rexlManu
 * https://rexlGames.de
 * Coded with IntelliJ
 */
public class Hologram {
    private Location location;
    private List<String> lines;
    private double distance_above = -0.27D;
    private List<EntityArmorStand> armorstands = new ArrayList<>();

    public Hologram(final Location loc, final String... lines) {
        this.location = loc;
        this.lines = Arrays.asList(lines);
    }

    public Hologram(final Location loc, final List<String> lines) {
        this.location = loc;
        this.lines = lines;
    }

    public List<String> getLines() {
        return this.lines;
    }

    public Location getLocation() {
        return this.location;
    }

    public void send(final Player p) {
        double y = this.getLocation().getY();
        for (int i = 0; i <= this.lines.size() - 1; i++) {
            y += this.distance_above;
            final EntityArmorStand eas = this.getEntityArmorStand(y);
            eas.setCustomName((String) this.lines.get(i));
            this.display(p, eas);
            this.armorstands.add(eas);
        }
    }


    public void setValue(final Object obj, final String name, final Object value) {
        try {
            final Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (final Exception localException) {
        }
    }

    public int getFixLocation(final double pos) {
        return MathHelper.floor(pos * 32.0D);
    }

    public byte getFixRotation(final float yawpitch) {
        return (byte) (int) (yawpitch * 256.0F / 360.0F);
    }

    public void destroy(final Player p) {
        for (final EntityArmorStand eas : this.armorstands) {
            final PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[]{eas.getId()});
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
        }
    }

    public void destroy() {
        for (final Player p : Bukkit.getOnlinePlayers()) {
            this.destroy(p);
        }
    }

    public void broadcast() {
        for (final Player p : Bukkit.getOnlinePlayers()) {
            this.send(p);
        }
    }

    public void broadcast(final List<Player> players) {
        for (final Player p : players) {
            this.send(p);
        }
    }

    private EntityArmorStand getEntityArmorStand(final double y) {
        final WorldServer world = ((CraftWorld) this.getLocation().getWorld()).getHandle();
        final EntityArmorStand eas = new EntityArmorStand(world);
        eas.setLocation(this.getLocation().getX(), y, this.getLocation().getZ(), 0.0F, 0.0F);
        eas.setInvisible(true);
        eas.setCustomNameVisible(true);
        return eas;
    }

    private void display(final Player p, final EntityArmorStand eas) {
        final PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(eas);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
    }

    public void setArmorstands(final List<EntityArmorStand> armorstands) {
        this.armorstands = armorstands;
    }

    public void setDistance_above(final double distance_above) {
        this.distance_above = distance_above;
    }

    public void setLines(final List<String> lines) {
        this.lines = lines;
    }

    public void setLines(final String... lines) {
        this.lines = Arrays.asList(lines);
    }

    public void setLocation(final Location location) {
        this.location = location;
    }

    public double getDistance_above() {
        return this.distance_above;
    }

    public List<EntityArmorStand> getArmorstands() {
        return this.armorstands;
    }
}
