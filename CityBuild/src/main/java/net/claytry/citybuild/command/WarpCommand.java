package net.claytry.citybuild.command;

import net.claytry.citybuild.CityBuild;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public final class WarpCommand implements CommandExecutor, Listener {

    private CityBuild cityBuild;

    public WarpCommand(CityBuild cityBuild) {
        this.cityBuild = cityBuild;
        Bukkit.getPluginManager().registerEvents(this, cityBuild);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        return false;
    }
}
