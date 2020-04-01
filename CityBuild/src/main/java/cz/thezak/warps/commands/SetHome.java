package cz.thezak.warps.commands;

import cz.thezak.warps.Warps;
import net.claytry.citybuild.utils.TitleAPI;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.configuration.*;

import java.util.*;

import org.bukkit.*;

public class SetHome implements CommandExecutor {
    private int x;

    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        Player p = null;
        if (sender instanceof Player) {
            p = (Player) sender;
        }
        if (command.getName().equalsIgnoreCase("sethome")) {
            if (p == null) {
                sender.sendMessage(Warps.plugin.getConfig().getString("Messages.NoPlayer").replaceAll("&", "§"));
                return true;
            }
            final ConfigurationSection homeSection = Warps.plugin.getConfig().getConfigurationSection("homes." + p.getUniqueId());
            int i = 0;
            try {
                for (final String homes : homeSection.getKeys(false)) {
                    ++i;
                }
            } catch (Exception ex) {
            }
            if (!p.hasPermission("warps.sethome")) {
                final Location location = p.getLocation();
                p.playSound(location, Sound.valueOf(Warps.plugin.getConfig().getString("Sounds.NoPermissionsSound")), 1.0f, 0.0f);
                TitleAPI.sendTitle(p, 10, 40, 20, "", Warps.plugin.getConfig().getString("Messages.NoPermissions").replaceAll("&", "§"));
            } else {
                if (p.hasPermission("warps.homes.op")) {
                    this.x = 54;
                } else if (p.hasPermission("warps.homes.1")) {
                    this.x = 1;
                } else if (p.hasPermission("warps.homes.2")) {
                    this.x = 2;
                } else if (p.hasPermission("warps.homes.3")) {
                    this.x = 3;
                } else if (p.hasPermission("warps.homes.4")) {
                    this.x = 4;
                } else if (p.hasPermission("warps.homes.5")) {
                    this.x = 5;
                } else if (p.hasPermission("warps.homes.6")) {
                    this.x = 6;
                } else if (p.hasPermission("warps.homes.7")) {
                    this.x = 7;
                } else if (p.hasPermission("warps.homes.8")) {
                    this.x = 8;
                } else if (p.hasPermission("warps.homes.9")) {
                    this.x = 9;
                } else if (p.hasPermission("warps.homes.10")) {
                    this.x = 10;
                } else if (p.hasPermission("warps.homes.11")) {
                    this.x = 11;
                } else if (p.hasPermission("warps.homes.12")) {
                    this.x = 12;
                } else if (p.hasPermission("warps.homes.13")) {
                    this.x = 13;
                } else if (p.hasPermission("warps.homes.14")) {
                    this.x = 14;
                } else if (p.hasPermission("warps.homes.15")) {
                    this.x = 15;
                } else if (p.hasPermission("warps.homes.16")) {
                    this.x = 16;
                } else if (p.hasPermission("warps.homes.17")) {
                    this.x = 17;
                } else if (p.hasPermission("warps.homes.18")) {
                    this.x = 18;
                } else if (p.hasPermission("warps.homes.19")) {
                    this.x = 19;
                } else if (p.hasPermission("warps.homes.20")) {
                    this.x = 20;
                } else if (p.hasPermission("warps.homes.21")) {
                    this.x = 21;
                } else if (p.hasPermission("warps.homes.22")) {
                    this.x = 22;
                } else if (p.hasPermission("warps.homes.23")) {
                    this.x = 23;
                } else if (p.hasPermission("warps.homes.24")) {
                    this.x = 24;
                } else if (p.hasPermission("warps.homes.25")) {
                    this.x = 25;
                } else if (p.hasPermission("warps.homes.26")) {
                    this.x = 26;
                } else if (p.hasPermission("warps.homes.27")) {
                    this.x = 27;
                } else if (p.hasPermission("warps.homes.28")) {
                    this.x = 28;
                } else if (p.hasPermission("warps.homes.29")) {
                    this.x = 29;
                } else if (p.hasPermission("warps.homes.30")) {
                    this.x = 30;
                } else if (p.hasPermission("warps.homes.31")) {
                    this.x = 31;
                } else if (p.hasPermission("warps.homes.32")) {
                    this.x = 32;
                } else if (p.hasPermission("warps.homes.33")) {
                    this.x = 33;
                } else if (p.hasPermission("warps.homes.34")) {
                    this.x = 34;
                } else if (p.hasPermission("warps.homes.35")) {
                    this.x = 35;
                } else if (p.hasPermission("warps.homes.36")) {
                    this.x = 36;
                } else if (p.hasPermission("warps.homes.37")) {
                    this.x = 37;
                } else if (p.hasPermission("warps.homes.38")) {
                    this.x = 38;
                } else if (p.hasPermission("warps.homes.39")) {
                    this.x = 39;
                } else if (p.hasPermission("warps.homes.40")) {
                    this.x = 40;
                } else if (p.hasPermission("warps.homes.41")) {
                    this.x = 41;
                } else if (p.hasPermission("warps.homes.42")) {
                    this.x = 42;
                } else if (p.hasPermission("warps.homes.43")) {
                    this.x = 43;
                } else if (p.hasPermission("warps.homes.44")) {
                    this.x = 44;
                } else if (p.hasPermission("warps.homes.45")) {
                    this.x = 45;
                } else if (p.hasPermission("warps.homes.46")) {
                    this.x = 46;
                } else if (p.hasPermission("warps.homes.47")) {
                    this.x = 47;
                } else if (p.hasPermission("warps.homes.48")) {
                    this.x = 48;
                } else if (p.hasPermission("warps.homes.49")) {
                    this.x = 49;
                } else if (p.hasPermission("warps.homes.50")) {
                    this.x = 50;
                } else if (p.hasPermission("warps.homes.51")) {
                    this.x = 51;
                } else if (p.hasPermission("warps.homes.52")) {
                    this.x = 52;
                } else if (p.hasPermission("warps.homes.53")) {
                    this.x = 53;
                } else if (p.hasPermission("warps.homes.54")) {
                    this.x = 54;
                } else {
                    final Location location = p.getLocation();
                    p.playSound(location, Sound.valueOf(Warps.plugin.getConfig().getString("Sounds.NoPermissionsSound")), 1.0f, 0.0f);
                    TitleAPI.sendTitle(p, 10, 40, 20, "", "" + Warps.plugin.getConfig().getString("Messages.NoPermissions").replaceAll("&", "§"));
                }
                if (i < this.x || this.x == 0) {
                    if (args.length == 1) {
                        final Location l = p.getLocation();
                        Warps.plugin.getConfig().set("homes." + p.getUniqueId() + "." + args[0] + ".x", (Object) (float) l.getBlockX());
                        Warps.plugin.getConfig().set("homes." + p.getUniqueId() + "." + args[0] + ".y", (Object) (float) l.getBlockY());
                        Warps.plugin.getConfig().set("homes." + p.getUniqueId() + "." + args[0] + ".z", (Object) (float) l.getBlockZ());
                        Warps.plugin.getConfig().set("homes." + p.getUniqueId() + "." + args[0] + ".v", (Object) l.getPitch());
                        Warps.plugin.getConfig().set("homes." + p.getUniqueId() + "." + args[0] + ".w", (Object) l.getYaw());
                        Warps.plugin.getConfig().set("homes." + p.getUniqueId() + "." + args[0] + ".world", (Object) p.getWorld().getName());
                        Warps.plugin.saveConfig();
                        Warps.plugin.reloadConfig();
                        TitleAPI.sendTitle(p, 10, 40, 20, "", "" + Warps.plugin.getConfig().getString("Messages.HomeCreated").replaceAll("&", "§"));
                        final Location location2 = p.getLocation();
                        p.playSound(location2, Sound.valueOf(Warps.plugin.getConfig().getString("Sounds.HomeCreatedSound")), 1.0f, 0.0f);
                    } else {
                        TitleAPI.sendTitle(p, 10, 40, 20, "", "" + Warps.plugin.getConfig().getString("Messages.ToSetHome").replaceAll("&", "§"));
                        final Location location = p.getLocation();
                        p.playSound(location, Sound.valueOf(Warps.plugin.getConfig().getString("Sounds.ToSetHomeSound")), 1.0f, 0.0f);
                    }
                } else {
                    final Location location = p.getLocation();
                    p.playSound(location, Sound.valueOf(Warps.plugin.getConfig().getString("Sounds.NoMoreHomesSound")), 1.0f, 0.0f);
                    TitleAPI.sendTitle(p, 10, 40, 20, "", "" + Warps.plugin.getConfig().getString("Messages.NoMoreHomes").replaceAll("&", "§"));
                }
            }
        }
        return true;
    }
}

