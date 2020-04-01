package cz.thezak.warps.commands;

import cz.thezak.warps.Warps;
import net.claytry.citybuild.utils.TitleAPI;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.*;

public class RemoveWarp implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        Player p = null;
        if (sender instanceof Player) {
            p = (Player)sender;
        }
        if (!command.getName().equalsIgnoreCase("removewarp")) {
            return true;
        }
        if (p == null) {
            sender.sendMessage(Warps.plugin.getConfig().getString("Messages.NoPlayer").replaceAll("&", "§"));
            return true;
        }
        if (p.hasPermission("warps.removewarp")) {
            if (args.length == 1) {
                if (Warps.plugin.getConfig().get("warps." + args[0]) == null) {
                    TitleAPI.sendTitle(p, 10, 15, 20, "", Warps.plugin.getConfig().getString("Messages.NoWarpWithName").replaceAll("&", "§") + " " + args[0]);
                    final Location location = p.getLocation();
                    p.playSound(location, Sound.valueOf(Warps.plugin.getConfig().getString("Sounds.NoWarpWithNameSound")), 1.0f, 0.0f);
                }
                else {
                    Warps.plugin.getConfig().getConfigurationSection("warps").set(args[0], (Object)null);
                    Warps.plugin.saveConfig();
                    TitleAPI.sendTitle(p, 10, 40, 20, "", "" + Warps.plugin.getConfig().getString("Messages.WarpRemoved").replaceAll("&", "§"));
                }
            }
            else {
                final Location location = p.getLocation();
                p.playSound(location, Sound.valueOf(Warps.plugin.getConfig().getString("Sounds.ToRemoveWarpSound")), 1.0f, 0.0f);
                TitleAPI.sendTitle(p, 10, 40, 20, "", "" + Warps.plugin.getConfig().getString("Messages.ToRemoveWarp").replaceAll("&", "§"));
            }
            return true;
        }
        final Location location = p.getLocation();
        p.playSound(location, Sound.valueOf(Warps.plugin.getConfig().getString("Sounds.NoPermissionsSound")), 1.0f, 0.0f);
        TitleAPI.sendTitle(p, 10, 40, 20, "", "" + Warps.plugin.getConfig().getString("Messages.NoPermissions").replaceAll("&", "§"));
        return true;
    }
}
