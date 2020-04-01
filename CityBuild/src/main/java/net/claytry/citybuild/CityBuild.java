package net.claytry.citybuild;

import com.jcdesimp.landlord.Landlord;
import cz.thezak.warps.Warps;
import lombok.Getter;
import net.claytry.citybuild.adminshop.AdminShopManager;
import net.claytry.citybuild.command.*;
import net.claytry.citybuild.listener.AsyncPlayerChatListener;
import net.claytry.citybuild.listener.PlayerJoinListener;
import net.claytry.citybuild.manager.ArangoConnector;
import net.claytry.citybuild.manager.MoneyManager;
import net.claytry.citybuild.manager.PlayTimeManager;
import net.claytry.citybuild.manager.PlayerScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class CityBuild extends JavaPlugin {

    @Getter
    private PlayTimeManager playTimeManager;
    @Getter
    private PlayerScoreboard playerScoreboard;
    @Getter
    private ArangoConnector arangoConnector;
    @Getter
    private MoneyManager moneyManager;
    @Getter
    private String prefix = "§8» §2§lC§alayTry §8┃ §r";
    @Getter
    private File file;
    @Getter
    private YamlConfiguration configuration;
    private Landlord landlord;
    private Warps warps;

    @Getter
    private AdminShopManager adminShopManager;

    @Override
    public void onEnable() {
        this.landlord = new Landlord(this);
        this.landlord.onEnable();
        this.arangoConnector = new ArangoConnector();
        this.arangoConnector.connect();
        this.playTimeManager = new PlayTimeManager(this);
        this.playerScoreboard = new PlayerScoreboard(this);
        this.moneyManager = new MoneyManager(this);
        getDataFolder().mkdir();
        file = new File(getDataFolder(), "config.yml");
        this.configuration = YamlConfiguration.loadConfiguration(file);
        this.warps = new Warps(this);
        warps.onEnable();
        registerAll();
        File file = new File("./farmworld/");
        if (file.exists()) {
            FarmworldCommand.world = new WorldCreator("farmworld").createWorld();
        }

        this.adminShopManager = new AdminShopManager(this);
    }

    public void save() {
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(o -> {
            o.kickPlayer("Der Server restartet.");
        });
        this.landlord.onDisable();
        this.warps.onDisable();
    }

    public String consoleMessage() {
        return getPrefix() + "§7Bitte führe diesen Command nur als Spieler aus§8.";
    }

    public String getPrefix() {
        return prefix;
    }

    public String permissionMessage() {
        return getPrefix() + "§cDazu hast du keine Berechtigung§8.";
    }

    private void registerAll() {
        getCommand("adminshop").setExecutor(new AdminShopCommand(this));
        getCommand("money").setExecutor(new MoneyCommand(this));
        getCommand("setup").setExecutor(new SetupCommand(this));
        getCommand("spawn").setExecutor(new SpawnCommand(this));
        getCommand("farmworld").setExecutor(new FarmworldCommand(this));
        Bukkit.getPluginManager().registerEvents(new AsyncPlayerChatListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this), this);
    }
}
