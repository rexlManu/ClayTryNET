package net.claytry.citybuild.command;

import net.claytry.citybuild.CityBuild;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class SetupCommand implements CommandExecutor {

    private CityBuild cityBuild;

    public SetupCommand(CityBuild cityBuild) {
        this.cityBuild = cityBuild;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (!player.hasPermission("claytry.admin")) {
            player.sendMessage(cityBuild.permissionMessage());
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("setup setspawn");
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("setspawn")) {
                cityBuild.getConfiguration().set("Spawn", player.getLocation());
                cityBuild.save();
                sender.sendMessage("Spawn gesetzt.");
            }
        }

        return false;
    }
}
