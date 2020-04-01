package cz.thezak.warps.commands;

import cz.thezak.warps.Warps;
import cz.thezak.warps.utils.Inv;
import cz.thezak.warps.utils.Utils;
import net.claytry.citybuild.utils.TitleAPI;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.*;

public class SetWarp implements CommandExecutor
{
    private Inv inv;
    private Utils u;

    public SetWarp() {
        this.inv = new Inv();
        this.u = new Utils();
    }

    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        Player p = null;
        if (sender instanceof Player) {
            p = (Player)sender;
        }
        if (!command.getName().equalsIgnoreCase("setwarp")) {
            return true;
        }
        if (p == null) {
            sender.sendMessage(Warps.plugin.getConfig().getString("Messages.NoPlayer").replaceAll("&", "§"));
            return true;
        }
        if (p.hasPermission("warps.setwarp")) {
            if (args.length == 1) {
                final Location l = p.getLocation();
                Warps.plugin.getConfig().set("warps." + args[0] + ".x", (Object)(float)l.getBlockX());
                Warps.plugin.getConfig().set("warps." + args[0] + ".y", (Object)(float)l.getBlockY());
                Warps.plugin.getConfig().set("warps." + args[0] + ".z", (Object)(float)l.getBlockZ());
                Warps.plugin.getConfig().set("warps." + args[0] + ".yaws", (Object)l.getYaw());
                Warps.plugin.getConfig().set("warps." + args[0] + ".pitch", (Object)l.getPitch());
                Warps.plugin.getConfig().set("warps." + args[0] + ".world", (Object)p.getWorld().getName());
                Warps.plugin.saveConfig();
                Warps.plugin.reloadConfig();
                this.u.getWarp(args[0]);
                this.inv.editInv(p);
                this.u.setItem(new ItemStack(Material.BARRIER).getType().toString());
                TitleAPI.sendTitle(p, 10, 40, 20, "", "" + Warps.plugin.getConfig().getString("Messages.WarpCreated").replaceAll("&", "§"));
                final Location location = p.getLocation();
                p.playSound(location, Sound.valueOf(Warps.plugin.getConfig().getString("Sounds.WarpCreatedSound")), 1.0f, 0.0f);
            }
            else {
                TitleAPI.sendTitle(p, 10, 40, 20, "", "" + Warps.plugin.getConfig().getString("Messages.ToSetWarp").replaceAll("&", "§"));
                final Location location2 = p.getLocation();
                p.playSound(location2, Sound.valueOf(Warps.plugin.getConfig().getString("Sounds.ToSetWarpSound")), 1.0f, 0.0f);
            }
            return true;
        }
        final Location location2 = p.getLocation();
        p.playSound(location2, Sound.valueOf(Warps.plugin.getConfig().getString("Sounds.NoPermissionsSound")), 1.0f, 0.0f);
        TitleAPI.sendTitle(p, 10, 40, 20, "", Warps.plugin.getConfig().getString("Messages.NoPermissions").replaceAll("&", "§"));
        return true;
    }
}
