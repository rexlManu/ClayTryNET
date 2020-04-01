package net.claytry.citybuild.command;

import net.claytry.citybuild.CityBuild;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class SpawnCommand implements CommandExecutor {

    private CityBuild cityBuild;

    public SpawnCommand(CityBuild cityBuild) {
        this.cityBuild = cityBuild;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        player.teleport((Location) cityBuild.getConfiguration().get("Spawn"));
        player.sendMessage(cityBuild.getPrefix() + "§7Du wurdest zum §aSpawn §7teleportiert.");
        return false;
    }
}
