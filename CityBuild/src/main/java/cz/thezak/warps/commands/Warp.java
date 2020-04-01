package cz.thezak.warps.commands;

import cz.thezak.warps.Warps;
import cz.thezak.warps.utils.Inv;
import net.claytry.citybuild.utils.TitleAPI;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.*;

public class Warp implements CommandExecutor
{
    private Inv inv;

    public Warp() {
        this.inv = new Inv();
    }

    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        Player p = null;
        if (sender instanceof Player) {
            p = (Player)sender;
        }
        if (!command.getName().equalsIgnoreCase("warp")) {
            return true;
        }
        if (p == null) {
            sender.sendMessage(Warps.plugin.getConfig().getString("Messages.NoPlayer").replaceAll("&", "§"));
            return true;
        }
        if (p.hasPermission("warps.warp")) {
            if (Warps.plugin.getConfig().getString("warps.") != null) {
                this.inv.warpInv(p);
            }
            else {
                final Location location = p.getLocation();
                p.playSound(location, Sound.valueOf(Warps.plugin.getConfig().getString("Sounds.SetWarpFirstSound")), 1.0f, 0.0f);
                TitleAPI.sendTitle(p, 10, 40, 20, "", "" + Warps.plugin.getConfig().getString("Messages.SetWarpFirst").replaceAll("&", "§"));
            }
            return true;
        }
        final Location location = p.getLocation();
        p.playSound(location, Sound.valueOf(Warps.plugin.getConfig().getString("Sounds.NoPermissionsSound")), 1.0f, 0.0f);
        TitleAPI.sendTitle(p, 10, 40, 20, "", "" + Warps.plugin.getConfig().getString("Messages.NoPermissions").replaceAll("&", "§"));
        return true;
    }
}
