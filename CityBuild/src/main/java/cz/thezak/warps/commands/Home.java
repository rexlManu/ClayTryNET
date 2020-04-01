package cz.thezak.warps.commands;

import cz.thezak.warps.Warps;
import cz.thezak.warps.utils.Inv;
import net.claytry.citybuild.utils.TitleAPI;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.*;

public class Home implements CommandExecutor
{
    private Inv inv;

    public Home() {
        this.inv = new Inv();
    }

    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        Player p = null;
        if (sender instanceof Player) {
            p = (Player)sender;
        }
        if (!command.getName().equalsIgnoreCase("home")) {
            return true;
        }
        if (p == null) {
            sender.sendMessage(Warps.plugin.getConfig().getString("Messages.NoPlayer").replaceAll("&", "§"));
            return true;
        }
        if (p.hasPermission("warps.home")) {
            if (Warps.plugin.getConfig().getString("homes." + p.getUniqueId() + ".") != null) {
                this.inv.homeInv(p);
            }
            else {
                final Location location = p.getLocation();
                p.playSound(location, Sound.valueOf(Warps.plugin.getConfig().getString("Sounds.SetHomeFirstSound")), 1.0f, 0.0f);
                TitleAPI.sendTitle(p, 10, 40, 20, "", "" + Warps.plugin.getConfig().getString("Messages.SetHomeFirst").replaceAll("&", "§"));
            }
            return true;
        }
        final Location location = p.getLocation();
        p.playSound(location, Sound.valueOf(Warps.plugin.getConfig().getString("Sounds.NoPermissionsSound")), 1.0f, 0.0f);
        TitleAPI.sendTitle(p, 10, 40, 20, "", Warps.plugin.getConfig().getString("Messages.NoPermissions").replaceAll("&", "§"));
        return true;
    }
}
