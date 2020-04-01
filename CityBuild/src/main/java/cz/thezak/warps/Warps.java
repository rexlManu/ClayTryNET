package cz.thezak.warps;

import cz.thezak.warps.commands.*;
import cz.thezak.warps.listener.InventoryListener;
import net.claytry.citybuild.CityBuild;
import org.bukkit.command.CommandExecutor;

public class Warps {
    public static CityBuild plugin;

    private CityBuild cityBuild;

    public Warps(CityBuild cityBuild) {
        this.cityBuild = cityBuild;
    }

    public void onEnable() {
        Warps.plugin = cityBuild;
        new InventoryListener(Warps.plugin);
        //plugin.getCommand("menu").setExecutor( new menu());
        //plugin.getCommand("setspawn").setExecutor( new setspawn());
        plugin.getCommand("setwarp").setExecutor( new SetWarp());
        plugin.getCommand("warp").setExecutor( new Warp());
        //plugin.getCommand("spawn").setExecutor( new spawn());
        plugin.getCommand("sethome").setExecutor( new SetHome());
        plugin.getCommand("home").setExecutor( new Home());
        //plugin.getCommand("togglewatch").setExecutor( new togglewatch());
        plugin.getCommand("removehome").setExecutor( new RemoveHome());
        plugin.getCommand("removewarp").setExecutor( new RemoveWarp());
        System.out.println("[Warps] Plugin has been activated!");
        this.config();
    }

    public void onDisable() {
        System.out.println("[Warps] Plugin has been Disabled!");
    }

    private void config() {
        plugin.reloadConfig();
        plugin.getConfig().addDefault("warps", (Object) "");
        plugin.getConfig().addDefault("homes", (Object) "");
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveConfig();
    }
}