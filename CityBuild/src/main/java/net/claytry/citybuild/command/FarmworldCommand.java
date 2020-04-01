package net.claytry.citybuild.command;

import net.claytry.citybuild.CityBuild;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class FarmworldCommand implements CommandExecutor {

    private CityBuild cityBuild;
    public static World world;

    public FarmworldCommand(CityBuild cityBuild) {
        this.cityBuild = cityBuild;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("claytry.admin"))
            return true;

        Player player = (Player) sender;
        if (world == null) {
            world = new WorldCreator("farmworld").createWorld();
            player.sendMessage("welt erstellt");
        } else {
            player.sendMessage("welt gibs");
            player.teleport(world.getSpawnLocation());
        }
        return false;
    }
}
