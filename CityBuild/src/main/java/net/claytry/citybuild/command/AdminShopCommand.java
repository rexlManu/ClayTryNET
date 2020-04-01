package net.claytry.citybuild.command;

import net.claytry.citybuild.CityBuild;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class AdminShopCommand implements CommandExecutor {

    private CityBuild cityBuild;

    public AdminShopCommand(CityBuild cityBuild) {
        this.cityBuild = cityBuild;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        cityBuild.getAdminShopManager().openMainInventory(player);
        player.playSound(player.getLocation(), Sound.CHEST_OPEN, 10f, 10f);
        return false;
    }
}
