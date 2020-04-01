package cz.thezak.warps.commands;

import cz.thezak.warps.Warps;
import net.claytry.citybuild.utils.TitleAPI;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.*;

public class RemoveHome implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        Player p = null;
        if (sender instanceof Player) {
            p = (Player)sender;
        }
        if (command.getName().equalsIgnoreCase("removehome")) {
            if (p == null) {
                sender.sendMessage(Warps.plugin.getConfig().getString("Messages.NoPlayer").replaceAll("&", "§"));
                return true;
            }
            if (args.length == 0) {
                TitleAPI.sendTitle(p, 10, 40, 20, "", "" + Warps.plugin.getConfig().getString("Messages.ToRemoveHome").replaceAll("&", "§"));
            }
            if (args.length != 1) {
                final Location location = p.getLocation();
                p.playSound(location, Sound.valueOf(Warps.plugin.getConfig().getString("Sounds.NoPermissionsSound")), 1.0f, 0.0f);
                TitleAPI.sendTitle(p, 10, 40, 20, "", "" + Warps.plugin.getConfig().getString("Messages.NoPermissions").replaceAll("&", "§"));
                return true;
            }
            if (p.hasPermission("warps.removehome")) {
                if (Warps.plugin.getConfig().get("homes." + p.getUniqueId() + "." + args[0]) == null) {
                    TitleAPI.sendTitle(p, 10, 15, 20, "", "" + Warps.plugin.getConfig().getString("Messages.NoHomeWithName").replaceAll("&", "§") + " " + args[0]);
                    final Location location = p.getLocation();
                    p.playSound(location, Sound.valueOf(Warps.plugin.getConfig().getString("Sounds.NoHomeWithNameSound")), 1.0f, 0.0f);
                }
                else {
                    Warps.plugin.getConfig().getConfigurationSection("homes." + p.getUniqueId()).set(args[0], (Object)null);
                    Warps.plugin.saveConfig();
                    TitleAPI.sendTitle(p, 10, 40, 20, "", "" + Warps.plugin.getConfig().getString("Messages.HomeRemoved").replaceAll("&", "§"));
                }
            }
            else {
                TitleAPI.sendTitle(p, 10, 40, 20, "", "" + Warps.plugin.getConfig().getString("Messages.ToRemoveHome").replaceAll("&", "§"));
            }
            final Location location = p.getLocation();
            p.playSound(location, Sound.valueOf(Warps.plugin.getConfig().getString("Sounds.ToRemoveHomeSound")), 1.0f, 0.0f);
        }
        return true;
    }
}

