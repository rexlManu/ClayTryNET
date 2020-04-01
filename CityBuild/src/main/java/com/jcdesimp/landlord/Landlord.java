package com.jcdesimp.landlord;

import com.avaje.ebean.EbeanServer;
import com.jcdesimp.landlord.configuration.CustomConfig;
import com.jcdesimp.landlord.landFlags.*;
import com.jcdesimp.landlord.landManagement.FlagManager;
import com.jcdesimp.landlord.landManagement.ViewManager;
import com.jcdesimp.landlord.landMap.MapManager;
import com.jcdesimp.landlord.persistantData.*;
import com.jcdesimp.landlord.pluginHooks.WorldguardHandler;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import net.claytry.citybuild.CityBuild;
import net.claytry.citybuild.manager.MoneyManager;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.createInventory;
import static org.bukkit.Bukkit.getOfflinePlayer;

//import com.lennardf1989.bukkitex.MyDatabase;
//import com.jcdesimp.landlord.landManagement.LandListener;

/**
 * Main plugin class for Landlord
 */
public final class Landlord  {

    private MyDatabase database;
    private Landlord plugin;
    private MapManager mapManager;
    private WorldguardHandler wgHandler;
    private FlagManager flagManager;
    private ViewManager manageViewManager;
    private LandAlerter pListen;

    private CustomConfig mainConfig;
    private CustomConfig messagesConfig;

    private CityBuild cityBuild;
    private static Landlord instance;

    public Landlord(CityBuild cityBuild) {
        this.cityBuild = cityBuild;
        instance = this;
    }

    public static Landlord getInstance() {
        return instance;
        //return Bukkit.getPluginManager().getPlugin("MyPlugin");
    }

    public CityBuild getCityBuild() {
        return cityBuild;
    }

    public void onEnable() {
        plugin = this;
        mapManager = new MapManager(this);
        //listner = new LandListener();
        //getServer().getPluginManager().registerEvents(new LandListener(this), this);
        flagManager = new FlagManager(this);
        manageViewManager = new ViewManager();
        cityBuild.getServer().getPluginManager().registerEvents(mapManager, cityBuild);


        // generate/load the main config file
        mainConfig = new CustomConfig(cityBuild, "config.yml", "config.yml");
        // generate/load the main language file based on language value in config.
        messagesConfig = new CustomConfig(cityBuild, "messages/english.yml", "messages/" + (mainConfig.get().getString("options.messagesFile").replace("/", ".")));
        // Registering Alert Listener
        pListen = new LandAlerter(plugin);
        if (getConfig().getBoolean("options.showLandAlerts", true)) {
            cityBuild.getServer().getPluginManager().registerEvents(pListen, cityBuild);
        }


        // Database creation, configuration, and maintenance.
        setupDatabase();
        //getLogger().info(getDescription().getName() + ": Created by Jcdesimp");
        cityBuild.getLogger().info("Created by Jcdesimp!");


        //Plugin Metrics
        try {
            Metrics metrics = new Metrics(cityBuild);
            metrics.start();
        } catch (IOException e) {
            // Failed to submit the stats :-(
        }


        // Command Executor
        cityBuild.getCommand("landlord").setExecutor(new LandlordCommandExecutor(this));

        //Worldguard Check
        if (!hasWorldGuard() && this.getConfig().getBoolean("worldguard.blockRegionClaim", true)) {
            cityBuild.getLogger().warning("Worldguard not found, worldguard features disabled.");
        } else if (hasWorldGuard()) {
            cityBuild.getLogger().info("Worldguard found!");
            wgHandler = new WorldguardHandler(getWorldGuard());
        }

        /*//Vault Check
        if (!hasVault() && this.getConfig().getBoolean("economy.enable", true)) {
            cityBuild.getLogger().warning("Vault not found, economy features disabled.");
        } else if (hasVault()) {
            cityBuild.getLogger().info("Vault found!");
            vHandler = new VaultHandler();
            if (!vHandler.hasEconomy()) {
                cityBuild. getLogger().warning("No economy found, economy features disabled.");
            }
        }**/

        verifyDatabaseVersion();


        //Register default flags
        if (getConfig().getBoolean("enabled-flags.build")) {
            flagManager.registerFlag(new Build(this));
        }
        if (getConfig().getBoolean("enabled-flags.harmAnimals")) {
            flagManager.registerFlag(new HarmAnimals(this));
        }
        if (getConfig().getBoolean("enabled-flags.useContainers")) {
            flagManager.registerFlag(new UseContainers(this));
        }
        if (getConfig().getBoolean("enabled-flags.tntDamage")) {
            flagManager.registerFlag(new TntDamage(this));
        }
        if (getConfig().getBoolean("enabled-flags.useRedstone")) {
            flagManager.registerFlag(new UseRedstone(this));
        }
        if (getConfig().getBoolean("enabled-flags.openDoor")) {
            flagManager.registerFlag(new OpenDoor(this));
        }
        if (getConfig().getBoolean("enabled-flags.pvp")) {
            flagManager.registerFlag(new PVP(this));
        }
        //flagManager.registerFlag(new OpenDoorDUPE1());
        //flagManager.registerFlag(new OpenDoorDUPE2());
        //flagManager.registerFlag(new OpenDoorDUPE3());


    }

    public void onDisable() {
        cityBuild.getLogger().info(cityBuild.getDescription().getName() + " has been disabled!");
        mapManager.removeAllMaps();
        manageViewManager.deactivateAll();
        pListen.clearPtrack();
    }

    public FileConfiguration getConfig() {
        return mainConfig.get();
    }

    public FileConfiguration getMessageConfig() {
        return messagesConfig.get();
    }

    private FileConfiguration getMessages() {
        return messagesConfig.get();
    }

    public FlagManager getFlagManager() {
        return flagManager;
    }

    public MapManager getMapManager() {
        return mapManager;
    }

    public ViewManager getManageViewManager() {
        return manageViewManager;
    }



    /*
     * ***************************
     *      Dependency Stuff
     * ***************************
     */

    /*
     * **************
     *   Worldguard
     * **************
     */
    private WorldGuardPlugin getWorldGuard() {
        Plugin plugin = cityBuild.getServer().getPluginManager().getPlugin("WorldGuard");

        // WorldGuard may not be loaded
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null; // Maybe you want throw an exception instead
        }

        return (WorldGuardPlugin) plugin;
    }


    /**
     * Provides access to the Landlord WorldGuardHandler
     *
     * @return ll wg handler
     */
    public WorldguardHandler getWgHandler() {
        return wgHandler;
    }

    public boolean hasWorldGuard() {
        Plugin plugin =  cityBuild.getServer().getPluginManager().getPlugin("WorldGuard");

        //System.out.println("-------- " + plugin.toString());
        // WorldGuard may not be loaded
        if (plugin == null || !(plugin instanceof WorldGuardPlugin) || !this.getConfig().getBoolean("worldguard.blockRegionClaim", true)) {
            return false;
        }
        /*if(plugin.toString().contains("6.0.0-beta")) {

            getLogger().warning("This WorldGuard version \'6.0.0-beta\' does not work with Landlord, please update it.");
            return false;
        }*/

        return true;
    }

    /*
     * **************
     *     Vault
     * **************
     */

    public MoneyManager getvHandler() {
        return cityBuild.getMoneyManager();
    }



    /*
     * ***************************
     *       Database Stuff
     * ***************************
     */


    private void setupDatabase() {
        Configuration config = getConfig();

        database = new MyDatabase(cityBuild) {
            protected java.util.List<Class<?>> getDatabaseClasses() {
                List<Class<?>> list = new ArrayList<Class<?>>();
                list.add(OwnedLand.class);
                list.add(Friend.class);
                list.add(DBVersion.class);
                list.add(LandFlagPerm.class);

                return list;
            }
        };

        database.initializeDatabase(
                config.getString("database.driver", "org.sqlite.JDBC"),
                config.getString("database.url", "jdbc:sqlite:{DIR}{NAME}.db"),
                config.getString("database.username", "bukkit"),
                config.getString("database.password", "walrus"),
                config.getString("database.isolation", "SERIALIZABLE"),
                config.getBoolean("database.logging", false),
                config.getBoolean("database.rebuild", false)
        );

        config.set("database.rebuild", false);

    }

    /*@Override
    public List<Class<?>> getDatabaseClasses() {
        List<Class<?>> list = new ArrayList<Class<?>>();
        list.add(OwnedLand.class);
        list.add(Friend.class);
        //list.add(OwnedLand_Friend.class);
        return list;
    }*/

    public EbeanServer getDatabase() {
        return database.getDatabase();
    }

    public void verifyDatabaseVersion() {
        int CURRENT_VERSION = 1;
        if (this.getDatabase().find(DBVersion.class).where().eq("identifier", "version").findUnique() == null) {
            //Convert Database
            this.cityBuild.getLogger().info("Starting OwnedLand conversion...");
            List<OwnedLand> allLand = plugin.getDatabase().find(OwnedLand.class).findList();
            for (OwnedLand l : allLand) {
                if (l.getOwnerName().length() < 32) {
                    //plugin.getLogger().info("Converting "+ l.getId() + "...");
                        /*
                         * *************************************
                         * mark for possible change    !!!!!!!!!
                         * *************************************
                         */
                    if (getOfflinePlayer(l.getOwnerName()).hasPlayedBefore()) {
                        //plugin.getLogger().info("Converting "+ l.getId() + "... Owner: "+l.getOwnerName());
                        l.setOwnerName(getOfflinePlayer(l.getOwnerName()).getUniqueId().toString());
                        plugin.getDatabase().save(l);

                    } else {
                        //plugin.getLogger().info("Deleting "+ l.getId() + "! Owner: "+l.getOwnerName());
                        plugin.getDatabase().delete(l);
                    }


                }
            }
            this.cityBuild.getLogger().info("Land Conversion completed!");

            this.cityBuild.getLogger().info("Starting Friend conversion...");
            List<Friend> allFriends = plugin.getDatabase().find(Friend.class).findList();
            for (Friend f : allFriends) {
                if (f.getPlayerName().length() < 32) {
                    //plugin.getLogger().info("Converting "+ l.getId() + "...");
                        /*
                         * *************************************
                         * mark for possible change    !!!!!!!!!
                         * *************************************
                         */
                    if (getOfflinePlayer(f.getPlayerName()).hasPlayedBefore()) {
                        //plugin.getLogger().info("Converting "+ f.getId() + "... Name: "+f.getPlayerName());
                        f.setPlayerName(getOfflinePlayer(f.getPlayerName()).getUniqueId().toString());
                        plugin.getDatabase().save(f);

                    } else {
                        //plugin.getLogger().info("Deleting "+ f.getId() + "! Name: "+f.getPlayerName());
                        plugin.getDatabase().delete(f);
                    }


                }
            }
            this.cityBuild.getLogger().info("Friend Conversion completed!");
            this.cityBuild.getLogger().info("Starting Permission conversion...");
            allLand = plugin.getDatabase().find(OwnedLand.class).findList();
            for (OwnedLand l : allLand) {
                if (l.getPermissions() != null) {


                    String[] currPerms = l.getPermissions().split("\\|");
                    String newPermString = "";
                    for (int i = 0; i < currPerms.length; i++) {
                        currPerms[i] = currPerms[i].substring(0, 3);
                        newPermString += Integer.toString(Integer.parseInt(currPerms[i], 2));
                        if (i < currPerms.length - 1) {
                            newPermString += "|";
                        }

                    }
                    l.setPermissions(newPermString);
                    plugin.getDatabase().save(l);
                }
            }
            //Entries for legacy flags
            this.getDatabase().save(LandFlagPerm.flagPermFromData("Build", 1));
            this.getDatabase().save(LandFlagPerm.flagPermFromData("HarmAnimals", 2));
            this.getDatabase().save(LandFlagPerm.flagPermFromData("UseContainers", 3));

            this.cityBuild.getLogger().info("Permission Conversion completed!");
            DBVersion vUpdate = new DBVersion();
            vUpdate.setIdentifier("version");
            vUpdate.setIntData(1);
            this.getDatabase().save(vUpdate);
        }
        int currVersion = this.getDatabase().find(DBVersion.class).where().eq("identifier", "version").findUnique().getIntData();
        if (currVersion < CURRENT_VERSION) {
            this.cityBuild.getLogger().info("Database outdated!");
        }

    }


}
