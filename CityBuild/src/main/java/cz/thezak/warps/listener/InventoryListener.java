package cz.thezak.warps.listener;

import cz.thezak.warps.utils.Inv;
import cz.thezak.warps.utils.Utils;
import net.claytry.citybuild.utils.TitleAPI;
import org.bukkit.plugin.java.*;
import org.bukkit.plugin.*;
import org.bukkit.event.inventory.*;
import org.bukkit.entity.*;
import cz.thezak.warps.*;
import org.bukkit.potion.*;
import org.bukkit.*;
import org.bukkit.event.*;

public class InventoryListener implements Listener
{
    private Inv i;
    private Utils u;

    public InventoryListener(final JavaPlugin plugin) {
        this.i = new Inv();
        this.u = new Utils();
        plugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
    }

    @EventHandler
    public void onClick(final InventoryClickEvent e) {
        final Player p = (Player)e.getWhoClicked();
        if (e.getSlot() >= 0) {
            if (e.getInventory().getTitle().equals(Warps.plugin.getConfig().getString("Messages.Menu").replaceAll("&", "§"))) {
                if (e.getSlot() == 10) {
                    if (Warps.plugin.getConfig().getString("warps.") != null) {
                        e.setCancelled(true);
                        p.closeInventory();
                        final Location location = p.getLocation();
                        p.playSound(location, Sound.valueOf(Warps.plugin.getConfig().getString("Sounds.ClickSound")), 1.0f, 0.0f);
                        this.i.warpInv(p);
                    }
                    else {
                        e.setCancelled(true);
                        TitleAPI.sendTitle(p, 10, 40, 20, "", "" + Warps.plugin.getConfig().getString("Messages.SetWarpFirst").replaceAll("&", "§"));
                        p.closeInventory();
                        final Location location = p.getLocation();
                        p.playSound(location, Sound.valueOf(Warps.plugin.getConfig().getString("Sounds.SetWarpFirstSound")), 1.0f, 0.0f);
                    }
                    if (!p.hasPermission("warps.warp")) {
                        final Location location = p.getLocation();
                        p.playSound(location, Sound.valueOf(Warps.plugin.getConfig().getString("Sounds.NoPermissionsSound")), 1.0f, 0.0f);
                        TitleAPI.sendTitle(p, 10, 40, 20, "", Warps.plugin.getConfig().getString("Messages.NoPermissions").replaceAll("&", "§"));
                        e.setCancelled(true);
                        p.closeInventory();
                    }
                }
                else if (e.getSlot() == 12) {
                    if (Warps.plugin.getConfig().getString("homes." + p.getUniqueId() + ".") != null) {
                        e.setCancelled(true);
                        p.closeInventory();
                        final Location location = p.getLocation();
                        p.playSound(location, Sound.valueOf(Warps.plugin.getConfig().getString("Sounds.ClickSound")), 1.0f, 0.0f);
                        this.i.homeInv(p);
                    }
                    else {
                        e.setCancelled(true);
                        TitleAPI.sendTitle(p, 10, 40, 20, "", "" + Warps.plugin.getConfig().getString("Messages.SetHomeFirst").replaceAll("&", "§"));
                        p.closeInventory();
                        final Location location = p.getLocation();
                        p.playSound(location, Sound.valueOf(Warps.plugin.getConfig().getString("Sounds.SetHomeFirstSound")), 1.0f, 0.0f);
                    }
                    if (!p.hasPermission("warps.home")) {
                        final Location location = p.getLocation();
                        p.playSound(location, Sound.valueOf(Warps.plugin.getConfig().getString("Sounds.NoPermissionsSound")), 1.0f, 0.0f);
                        TitleAPI.sendTitle(p, 10, 40, 20, "", Warps.plugin.getConfig().getString("Messages.NoPermissions").replaceAll("&", "§"));
                        e.setCancelled(true);
                        p.closeInventory();
                    }
                }
                else if (e.getSlot() == 14) {
                    if (p.hasPermission("warps.togglewatch")) {
                        final Location location = p.getLocation();
                        p.playSound(location, Sound.valueOf(Warps.plugin.getConfig().getString("Sounds.MagicWatchToggleSound")), 1.0f, 0.0f);
                        TitleAPI.sendTitle(p, 10, 40, 20, "", "" + Warps.plugin.getConfig().getString("Messages.MagicWatchToggle").replaceAll("&", "§"));
                        e.setCancelled(true);
                        p.closeInventory();
                        if (!Warps.plugin.getConfig().getBoolean("watch")) {
                            Warps.plugin.getConfig().set("watch", (Object)true);
                            Warps.plugin.saveConfig();
                        }
                        else if (Warps.plugin.getConfig().getBoolean("watch")) {
                            Warps.plugin.getConfig().set("watch", (Object)false);
                            Warps.plugin.saveConfig();
                        }
                        else {
                            Warps.plugin.getConfig().set("watch", (Object)true);
                            Warps.plugin.saveConfig();
                        }
                        Warps.plugin.reloadConfig();
                    }
                    else {
                        TitleAPI.sendTitle(p, 10, 40, 20, "", Warps.plugin.getConfig().getString("Messages.NoPermissions").replaceAll("&", "§"));
                        e.setCancelled(true);
                        final Location location = p.getLocation();
                        p.playSound(location, Sound.valueOf(Warps.plugin.getConfig().getString("Sounds.NoPermissionsSound")), 1.0f, 0.0f);
                        p.closeInventory();
                    }
                }
                else if (e.getSlot() == 22) {
                    final Location location = p.getLocation();
                    p.playSound(location, Sound.DOOR_CLOSE, 1.0f, 0.0f);
                    e.setCancelled(true);
                    p.closeInventory();
                }
                else if (e.getSlot() == 16) {
                    if (p.hasPermission("warps.spawn")) {
                        try {
                            final int x = Warps.plugin.getConfig().getInt("spawn.x");
                            final int y = Warps.plugin.getConfig().getInt("spawn.y");
                            final int z = Warps.plugin.getConfig().getInt("spawn.z");
                            final int pitch = Warps.plugin.getConfig().getInt("spawn.pitch");
                            final int yaw = Warps.plugin.getConfig().getInt("spawn.yaw");
                            final World world = Bukkit.getWorld(Warps.plugin.getConfig().getString("spawn.world"));
                            final Location l = new Location(world, (double)x, (double)y, (double)z, (float)yaw, (float)pitch);
                            p.teleport(l);
                            TitleAPI.sendTitle(p, 10, 40, 20, "", "" + Warps.plugin.getConfig().getString("Messages.TeleportedToSpawn").replaceAll("&", "§"));
                            final Location location2 = p.getLocation();
                            p.playSound(location2, Sound.valueOf(Warps.plugin.getConfig().getString("Sounds.TeleportedToSpawnSound")), 1.0f, 0.0f);
                            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 40));
                            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 40));
                        }
                        catch (Exception ex) {
                            TitleAPI.sendTitle(p, 10, 40, 20, "", "" + Warps.plugin.getConfig().getString("Messages.SetSpawnFirst").replaceAll("&", "§"));
                            final Location location3 = p.getLocation();
                            p.playSound(location3, Sound.valueOf(Warps.plugin.getConfig().getString("Sounds.SetSpawnFirstSound")), 1.0f, 0.0f);
                            e.setCancelled(true);
                            p.closeInventory();
                        }
                    }
                    else {
                        TitleAPI.sendTitle(p, 10, 40, 20, "", Warps.plugin.getConfig().getString("Messages.NoPermissions").replaceAll("&", "§"));
                        final Location location = p.getLocation();
                        p.playSound(location, Sound.valueOf(Warps.plugin.getConfig().getString("Sounds.NoPermissionsSound")), 1.0f, 0.0f);
                    }
                }
                else {
                    e.setCancelled(true);
                }
            }
            else if (e.getInventory().getTitle().equals(Warps.plugin.getConfig().getString("Messages.Warps").replaceAll("&", "§"))) {
                if (p.hasPermission("warps.warp")) {
                    if (e.getCurrentItem().getType() != Material.AIR) {
                        try {
                            final int x = Warps.plugin.getConfig().getInt("warps." + e.getCurrentItem().getItemMeta().getDisplayName() + ".x");
                            final int y = Warps.plugin.getConfig().getInt("warps." + e.getCurrentItem().getItemMeta().getDisplayName() + ".y");
                            final int z = Warps.plugin.getConfig().getInt("warps." + e.getCurrentItem().getItemMeta().getDisplayName() + ".z");
                            final int yaw2 = Warps.plugin.getConfig().getInt("warps." + e.getCurrentItem().getItemMeta().getDisplayName() + ".yaws");
                            final int pitch2 = Warps.plugin.getConfig().getInt("warps." + e.getCurrentItem().getItemMeta().getDisplayName() + ".pitch");
                            final World world = Bukkit.getWorld(Warps.plugin.getConfig().getString("warps." + e.getCurrentItem().getItemMeta().getDisplayName() + ".world"));
                            final Location l = new Location(world, (double)x, (double)y, (double)z, (float)yaw2, (float)pitch2);
                            p.teleport(l);
                            final Location location2 = p.getLocation();
                            p.playSound(location2, Sound.valueOf(Warps.plugin.getConfig().getString("Sounds.TeleportedToWarpSound")), 1.0f, 0.0f);
                            l.getWorld().playEffect(location2, Effect.MOBSPAWNER_FLAMES, 10000);
                            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 40));
                            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 40));
                            TitleAPI.sendTitle(p, 10, 40, 20, "", Warps.plugin.getConfig().getString("Messages.TeleportedToWarp").replaceAll("&", "§") + " " + e.getCurrentItem().getItemMeta().getDisplayName());
                        }
                        catch (Exception localException1) {
                            localException1.printStackTrace();
                        }
                    }
                }
                else {
                    TitleAPI.sendTitle(p, 10, 40, 20, "", Warps.plugin.getConfig().getString("Messages.NoPermissions").replaceAll("&", "§"));
                    final Location location = p.getLocation();
                    p.playSound(location, Sound.valueOf(Warps.plugin.getConfig().getString("Sounds.NoPermissionsSound")), 1.0f, 0.0f);
                    e.setCancelled(true);
                    p.closeInventory();
                }
                e.setCancelled(true);
            }
            else if (e.getInventory().getTitle().equals(Warps.plugin.getConfig().getString("Messages.Icons").replaceAll("&", "§"))) {
                final int slot = e.getSlot();
                if (slot >= 0) {
                    final Location location3 = p.getLocation();
                    p.playSound(location3, Sound.valueOf(Warps.plugin.getConfig().getString("Sounds.ClickSound")), 1.0f, 0.0f);
                    this.u.setItem(e.getCurrentItem().getType().toString());
                    p.closeInventory();
                    Warps.plugin.reloadConfig();
                }
                else {
                    p.closeInventory();
                    e.setCancelled(true);
                }
            }
            else if (e.getInventory().getTitle().equals(Warps.plugin.getConfig().getString("Messages.Homes").replaceAll("&", "§")) && e.getCurrentItem().getType() != Material.AIR) {
                if (Warps.plugin.getConfig().get("homes." + p.getUniqueId() + "." + e.getCurrentItem().getItemMeta().getDisplayName()) == null) {
                    e.setCancelled(true);
                }
                else if (p.hasPermission("warps.home")) {
                    if (e.getCurrentItem().getType() != Material.AIR) {
                        try {
                            final int x = Warps.plugin.getConfig().getInt("homes." + p.getUniqueId() + "." + e.getCurrentItem().getItemMeta().getDisplayName() + ".x");
                            final int y = Warps.plugin.getConfig().getInt("homes." + p.getUniqueId() + "." + e.getCurrentItem().getItemMeta().getDisplayName() + ".y");
                            final int z = Warps.plugin.getConfig().getInt("homes." + p.getUniqueId() + "." + e.getCurrentItem().getItemMeta().getDisplayName() + ".z");
                            final int yaw2 = Warps.plugin.getConfig().getInt("homes." + p.getUniqueId() + "." + e.getCurrentItem().getItemMeta().getDisplayName() + ".w");
                            final int pitch2 = Warps.plugin.getConfig().getInt("homes." + p.getUniqueId() + "." + e.getCurrentItem().getItemMeta().getDisplayName() + ".v");
                            final World world = Bukkit.getWorld(Warps.plugin.getConfig().getString("homes." + p.getUniqueId() + "." + e.getCurrentItem().getItemMeta().getDisplayName() + ".world"));
                            final Location l = new Location(world, (double)x, (double)y, (double)z, (float)yaw2, (float)pitch2);
                            p.teleport(l);
                            final Location location2 = p.getLocation();
                            p.playSound(location2, Sound.valueOf(Warps.plugin.getConfig().getString("Sounds.TeleportedToHomeSound")), 1.0f, 0.0f);
                            l.getWorld().playEffect(location2, Effect.MOBSPAWNER_FLAMES, 10000);
                            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 40));
                            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 40));
                            TitleAPI.sendTitle(p, 10, 40, 20, "", Warps.plugin.getConfig().getString("Messages.TeleportedToHome").replaceAll("&", "§") + " " + e.getCurrentItem().getItemMeta().getDisplayName());
                        }
                        catch (Exception localException2) {
                            localException2.printStackTrace();
                        }
                    }
                }
                else {
                    TitleAPI.sendTitle(p, 10, 40, 20, "", Warps.plugin.getConfig().getString("Messages.NoPermissions").replaceAll("&", "§"));
                    final Location location = p.getLocation();
                    p.playSound(location, Sound.valueOf(Warps.plugin.getConfig().getString("Sounds.NoPermissionsSound")), 1.0f, 0.0f);
                    e.setCancelled(true);
                    p.closeInventory();
                    e.setCancelled(true);
                }
            }
        }
    }
}

