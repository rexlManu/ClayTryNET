/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.lobbymodule;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.bridge.event.bukkit.BukkitOnlineCountUpdateEvent;
import de.dytanic.cloudnet.bridge.event.bukkit.BukkitPlayerUpdateEvent;
import de.dytanic.cloudnet.lib.player.permission.PermissionGroup;
import lombok.Getter;
import net.claytry.clayapi.both.Message;
import net.claytry.clayapi.both.user.User;
import net.claytry.clayapi.bukkit.bridge.BukkitPlayerBridge;
import net.claytry.clayapi.bukkit.manager.EventManager;
import net.claytry.clayapi.bukkit.manager.UserManager;
import net.claytry.clayapi.bukkit.misc.EventListener;
import net.claytry.clayapi.bukkit.utils.JsonConfig;
import net.claytry.clayapi.bukkit.utils.PlayerUtils;
import net.claytry.clayapi.module.Module;
import net.claytry.clayapi.module.ModuleConfig;
import net.claytry.clayapi.module.ModuleType;
import net.claytry.lobbymodule.gadget.GadgetManager;
import net.claytry.lobbymodule.gadget.NavigatorGadget;
import net.claytry.lobbymodule.gadget.PlayerhiderGadget;
import net.claytry.lobbymodule.gadget.SettingsGadget;
import net.claytry.lobbymodule.inventory.*;
import net.claytry.lobbymodule.playerhider.PlayerHider;
import net.claytry.lobbymodule.playtime.PlayTimeManager;
import net.claytry.lobbymodule.scoreboard.PlayerScoreboard;
import net.claytry.lobbymodule.settings.SettingManager;
import net.claytry.lobbymodule.utils.TitleAPI;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.IOException;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 28.07.2018 / 15:19                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/

public class LobbyModule extends Module {

    @Getter
    private static LobbyModule lobbyModule;

    @Getter
    private GadgetManager gadgetManager;
    @Getter
    private InventoryManager inventoryManager;
    @Getter
    private PlayerHider playerHider;
    @Getter
    private PlayerScoreboard playerScoreboard;
    @Getter
    private PlayTimeManager playTimeManager;
    @Getter
    private SettingManager settingManager;

    private String prefix;

    public LobbyModule() {
        super("Lobby", ModuleType.GAME);
        setAsync(true);
        lobbyModule = this;
        prefix = Message.generatePrefix("Lobby");

    }

    public void onEnable() {
        getModuleConfig().getJsonConfig().load();
        this.gadgetManager = new GadgetManager();
        this.inventoryManager = new InventoryManager();
        this.playerHider = new PlayerHider();
        this.playerScoreboard = new PlayerScoreboard();
        this.playTimeManager = new PlayTimeManager();
        this.settingManager = new SettingManager();

        registerAll();
    }

    private void registerAll() {
        this.gadgetManager.registerGadget(new NavigatorGadget());
        this.gadgetManager.registerGadget(new PlayerhiderGadget());
        this.gadgetManager.registerGadget(new SettingsGadget());
        this.inventoryManager.registerInventory(new NavigatorInventory());
        this.inventoryManager.registerInventory(new PlayerhiderInventory());
        this.inventoryManager.registerInventory(new InfoInventory());

        final EventManager eventManager = BukkitPlayerBridge.getInstance().getEventManager();
        eventManager.registerEvent(BlockPlaceEvent.class, (EventListener<BlockPlaceEvent>) event -> {
            event.setCancelled(event.getPlayer().getGameMode() != GameMode.CREATIVE);
        });
        eventManager.registerEvent(BlockBreakEvent.class, (EventListener<BlockBreakEvent>) event -> {
            event.setCancelled(event.getPlayer().getGameMode() != GameMode.CREATIVE);
        });
        eventManager.registerEvent(PlayerInteractEvent.class, (EventListener<PlayerInteractEvent>) event -> {
            event.setCancelled(event.getPlayer().getGameMode() != GameMode.CREATIVE);
        });
        eventManager.registerEvent(PlayerPickupItemEvent.class, (EventListener<PlayerPickupItemEvent>) event -> {
            event.setCancelled(event.getPlayer().getGameMode() != GameMode.CREATIVE);
        });
        eventManager.registerEvent(PlayerDropItemEvent.class, (EventListener<PlayerDropItemEvent>) event -> {
            event.setCancelled(event.getPlayer().getGameMode() != GameMode.CREATIVE);
        });
        eventManager.registerEvent(FoodLevelChangeEvent.class, (EventListener<FoodLevelChangeEvent>) event -> {
            event.setCancelled(true);
        });
        eventManager.registerEvent(WeatherChangeEvent.class, (EventListener<WeatherChangeEvent>) event -> {
            event.setCancelled(true);
        });
        eventManager.registerEvent(PlayerItemHeldEvent.class, (EventListener<PlayerItemHeldEvent>) event -> {
            final Player player = event.getPlayer();
            player.playSound(player.getLocation(), Sound.WOOD_CLICK, 10, 10);
        });
        eventManager.registerEvent(CreatureSpawnEvent.class, (EventListener<CreatureSpawnEvent>) event -> {
            event.setCancelled(event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.CUSTOM);
        });
        eventManager.registerEvent(EntityExplodeEvent.class, (EventListener<EntityExplodeEvent>) event -> {
            event.setCancelled(true);
        });
        eventManager.registerEvent(EntityDamageEvent.class, (EventListener<EntityDamageEvent>) event -> {
            event.setCancelled(true);
        });
        eventManager.registerEvent(EntityDamageByEntityEvent.class, (EventListener<EntityDamageByEntityEvent>) event -> {
            event.setCancelled(true);
        });
        eventManager.registerEvent(PlayerInteractAtEntityEvent.class, (EventListener<PlayerInteractAtEntityEvent>) event -> {
            if (event.getRightClicked() instanceof Player) {
                final Player target = (Player) event.getRightClicked();
                final Player player = event.getPlayer();

                inventoryManager.openInventory(player, ClickableInventoryType.INFO, target);
                player.playSound(player.getLocation(), Sound.CHEST_OPEN, 10L, 10L);

//                if (player.getPassenger() != null) return;
//                if (target.getPassenger() != null) return;
//
//                player.setPassenger(target.getPassenger());
//
//                player.playSound(player.getLocation(), Sound.ARROW_HIT, 1f, 1f);
//                target.playSound(player.getLocation(), Sound.ARROW_HIT, 1f, 1f);

            }
        });
        final JsonConfig jsonConfig = getModuleConfig().getJsonConfig();
        eventManager.registerEvent(PlayerJoinEvent.class, (EventListener<PlayerJoinEvent>) event -> {
            event.setJoinMessage(null);
            final Player player = event.getPlayer();
            player.setHealthScale(2);
            player.setHealth(player.getMaxHealth());
            player.setGameMode(GameMode.ADVENTURE);
            player.getInventory().clear();
            player.setFoodLevel(20);
            player.setPlayerTime(0, true);
            player.setLevel(0);
            player.setExp(0);
            if (jsonConfig.getLocation("Spawn") != null) {
                player.teleport(jsonConfig.getLocation("Spawn"));
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 2f, 2f);
            }
            TitleAPI.sendTitle(player, 5, 25, 5, "§8» §2§lC§alayTry.net", "§7Your Minigame§8-§7Server");
            gadgetManager.setGadgets(player);
            final int onlineCount = CloudAPI.getInstance().getOnlineCount();
            player.setLevel(onlineCount);
            player.setExp((float) onlineCount / (float) 150);

            PlayerUtils.parseThroughAllPlayer(o -> playerHider.setPlayerHiderStatus(playerHider.getPlayerHiderStatus(o), o));
            playTimeManager.loadPlayTime(player);
            settingManager.loadSettings(player);
            playerScoreboard.sendScoreboard(player);

            playerScoreboard.updateTablist();
        });

//        new BukkitRunnable() {
//
//            int i = 0;
//            private String[] words = new String[]{
//                    "Du", "Bist", "Geil", "Nicht", "❤", "Aber", "rexlManu", "ist", "dafür", "geil", "abonnier", "ihn", "doch", "auf", "youtube", "haha", "kevin"
//            };
//
//            @Override
//            public void run() {
//                if (i == words.length) {
//                    i = 0;
//                }
//                PlayerUtils.parseThroughAllPlayer(o -> {
//                    o.getScoreboard().getTeam("xD").setSuffix("§8» §4" + words[i]);
//                });
//
//                i++;
//            }
//        }.runTaskTimer(BukkitPlayerBridge.getInstance().getJavaPlugin(), 0, 20);

        eventManager.registerEvent(AsyncPlayerChatEvent.class, (EventListener<AsyncPlayerChatEvent>) event -> {
            final Player player = event.getPlayer();
            final PermissionGroup group = CloudAPI.getInstance().getOnlinePlayer(player.getUniqueId()).getPermissionEntity().getHighestPermissionGroup(CloudAPI.getInstance().getPermissionPool());
            String message = event.getMessage().replace("%", "%%");
            if (player.hasPermission("claytry.color")) {
                message = message.replace("°", "§");
            }
            event.setFormat(group.getDisplay() + player.getName() + " §8» §7" + message);
        });
        eventManager.registerEvent(PlayerQuitEvent.class, (EventListener<PlayerQuitEvent>) event -> {
            event.setQuitMessage(null);
            final Player player = event.getPlayer();
            playTimeManager.savePlayTime(player);
            settingManager.saveSettings(player);
        });
        eventManager.registerEvent(BukkitPlayerUpdateEvent.class, (EventListener<BukkitPlayerUpdateEvent>) event -> {
            if (Bukkit.getPlayer(event.getCloudPlayer().getUniqueId()) != null) {
                playerScoreboard.updateRank(Bukkit.getPlayer(event.getCloudPlayer().getUniqueId()));
            }
        });
        eventManager.registerEvent(BukkitOnlineCountUpdateEvent.class, (EventListener<BukkitOnlineCountUpdateEvent>) event -> {
            Bukkit.getOnlinePlayers().forEach(o -> {
                o.setLevel(event.getOnlineCount());
                o.setExp((float) event.getOnlineCount() / (float) 150);
            });
        });
        eventManager.onCommand("setup", (commandSender, command, s, strings) -> {
            if (!commandSender.hasPermission("claytry.admin")) {
                commandSender.sendMessage(Message.permissionMessage());
                return true;
            }
            final Player player = (Player) commandSender;
            if (strings.length == 1) {
                if (strings[0].equalsIgnoreCase("setspawn")) {
                    jsonConfig.setLocation("Spawn", player.getLocation());
                    try {
                        jsonConfig.save();
                    } catch (IOException e) {
                    }
                    player.sendMessage(prefix + "§7Du hast erfolgreich den §aSpawn §7gesetzt.");
                    return true;
                }
            } else if (strings.length == 2) {
                if (strings[0].equalsIgnoreCase("set")) {
                    jsonConfig.setLocation(strings[1], player.getLocation());
                    try {
                        jsonConfig.save();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    player.sendMessage(prefix + "§7Du hast erfolgreich den §a" + strings[1] + " §7gesetzt.");
                    return true;
                }
            }

            player.sendMessage(prefix + "§7/setup setspawn");
            player.sendMessage(prefix + "§7/setup set <Warp>");
            return false;
        });

    }

    public void onDisable() {

    }
}
