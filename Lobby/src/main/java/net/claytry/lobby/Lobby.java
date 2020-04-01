package net.claytry.lobby;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.bridge.event.bukkit.BukkitOnlineCountUpdateEvent;
import de.dytanic.cloudnet.bridge.event.bukkit.BukkitPlayerUpdateEvent;
import de.dytanic.cloudnet.lib.player.CloudPlayer;
import de.dytanic.cloudnet.lib.player.permission.PermissionGroup;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public final class Lobby extends JavaPlugin implements Listener {

    private Location spawn;
    private YamlConfiguration configuration;
    private File file;
    private String prefix = "§8» §2§lC§alayTry §8┃ §r";
    @Getter
    private ArangoConnector connector;
    @Getter
    private PlayerScoreboard playerScoreboard;
    @Getter
    private PlayTimeManager playTimeManager;

    @Override
    public void onEnable() {
        this.connector = new ArangoConnector();
        this.connector.connect();
        Bukkit.getPluginManager().registerEvents(this, this);
        getDataFolder().mkdir();
        this.file = new File(getDataFolder(), "config.yml");
        this.configuration = YamlConfiguration.loadConfiguration(file);
        if (!this.file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
            }
        }
        this.spawn = (Location) configuration.get("Spawn");
        this.playTimeManager = new PlayTimeManager(this);
        this.playerScoreboard = new PlayerScoreboard(this);

        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity.getType() != EntityType.ITEM_FRAME && entity.getType() != EntityType.ARMOR_STAND && entity.getType() != EntityType.PAINTING) {
                    entity.remove();
                }
            }
            world.setTime(0);
            world.setThunderDuration(0);
            world.setStorm(false);
            world.setThundering(false);
        }


        registerBungeeChannel(this, getServer());

        new BukkitRunnable() {
            @Override
            public void run() {
                for (World world : Bukkit.getWorlds()) {
                    world.setTime(0);
                    world.setThunderDuration(0);
                    world.setStorm(false);
                    world.setThundering(false);
                }
            }
        }.runTaskTimer(this, 0, 20 * 10);
    }

    @Override
    public void onDisable() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("setspawn"))
            if (!sender.hasPermission("claytry.admin")) {
                sender.sendMessage(permissionMessage());
                return true;
            }

        Player player = (Player) sender;
        configuration.set("Spawn", player.getLocation());
        player.sendMessage(prefix + "§7Du hast den Spawn gesetzt.");
        try {
            configuration.save(file);
        } catch (IOException e) {
        }

        return true;
    }

    public ItemStack createItem(String displayName, Material material) {
        ItemStack itemStack = new ItemStack(material, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack createItem(String displayName, Material material, int amount) {
        ItemStack itemStack = new ItemStack(material, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public void openNavigator(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 4 * 9, "§8» §2Teleporter");

        inventory.setItem(13, createItem("§8» §2CityBuild", Material.IRON_PICKAXE));

        player.openInventory(inventory);
    }

    public void openCityBuild(Inventory inventory, Player player) {

        inventory.setItem(20, setLore(createItem("§8» §2Alpha CityBuild", Material.IRON_PICKAXE, CloudAPI.getInstance().getOnlineCount("CityBuild")), "§7Server für die 1.8.8§8."));
        if (CloudAPI.getInstance().getServerInfo("CityBuild-1").isOnline()) {
        } else {
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!inventory.getViewers().contains(player)) {
                    cancel();
                }
                ItemStack item = inventory.getItem(20);
                if (CloudAPI.getInstance().getServerInfo("CityBuild-1").isOnline()) {
                    item.setAmount(CloudAPI.getInstance().getOnlineCount("CityBuild"));
                } else {
                    item.setAmount(0);
                }
            }
        }.runTaskTimer(this, 0, 20);
    }

    public ItemStack setLore(ItemStack itemStack, String... lore) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(Arrays.asList(lore));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @EventHandler
    public void on(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(null);
        getPlayTimeManager().savePlayTime(player);
    }

    @EventHandler
    public void on(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();
        player.setGameMode(GameMode.ADVENTURE);
        if (spawn != null) {
            player.teleport(spawn);
        }
        player.setFoodLevel(20);
        player.setHealthScale(2);
        player.setHealth(player.getMaxHealth());
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 1f);
        player.getInventory().clear();
        ItemStack itemStack = new ItemStack(Material.COMPASS, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§8» §2§lT§aeleporter §8┃ §7Rechtsklick");
        itemStack.setItemMeta(itemMeta);
        player.getInventory().setItem(4, itemStack);
        TitleAPI.sendTitle(player, 10, 20, 10, "§8» §2§lC§alayTry.net", "§7Willkommen§8, §a" + player.getName());
        PlayTime playTime = getPlayTimeManager().loadPlayTime(player);
        getPlayerScoreboard().sendScoreboard(player);
        getPlayerScoreboard().updatePlayTime(player, playTime);
        getPlayerScoreboard().updateTablist();
    }

    @EventHandler
    public void on(BukkitPlayerUpdateEvent event) {
        if (Bukkit.getPlayer(event.getCloudPlayer().getName()) != null) {
            Player player = Bukkit.getPlayer(event.getCloudPlayer().getName());
            getPlayerScoreboard().updateRank(player);
        }
    }

    @EventHandler
    public void on(InventoryClickEvent event) {
        if (event.getClickedInventory() != null) {
            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();
            if (event.getClickedInventory().getTitle().equalsIgnoreCase("§8» §2Teleporter")) {
                if (event.getCurrentItem() == null) return;
                if (!event.getCurrentItem().hasItemMeta()) return;
                if (!event.getCurrentItem().getItemMeta().hasDisplayName()) return;
                String displayName = event.getCurrentItem().getItemMeta().getDisplayName();
                if (displayName.equalsIgnoreCase("§8» §2CityBuild")) {
                    openCityBuild(event.getClickedInventory(), player);
                    player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1f, 1f);
                    return;
                } else if (displayName.equalsIgnoreCase("§8» §2Alpha CityBuild")) {
                    if (CloudAPI.getInstance().getServerInfo("CityBuild-1").isOnline()) {
                        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 1f);
                        sendPlayerToServer(this, player, "CityBuild-1");
                    } else {
                        player.sendMessage(prefix + "§7Dieser Server ist nicht online.");
                        player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1f, 1f);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        p.setAllowFlight(true);
        p.setFlying(false);
    }

    @EventHandler
    public void onPlayerFly(final PlayerToggleFlightEvent e) {
        final Player p = e.getPlayer();
        if (p.getGameMode() != GameMode.CREATIVE) {
            e.setCancelled(true);
            p.setAllowFlight(false);
            p.setFlying(false);
            p.setVelocity(p.getLocation().getDirection().multiply(2.0).setY(0.6));
            p.playEffect(p.getLocation(), Effect.SMOKE, 15);
        }
    }

    @EventHandler
    public void move(final PlayerMoveEvent e) {
        final Player p = e.getPlayer();
        if (e.getPlayer().getGameMode() != GameMode.CREATIVE && p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR) {
            p.setAllowFlight(true);
        }
    }

    @EventHandler
    public void on(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 10, 10);
    }

    @EventHandler
    public void on(AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        final PermissionGroup group = CloudAPI.getInstance().getOnlinePlayer(player.getUniqueId()).getPermissionEntity().getHighestPermissionGroup(CloudAPI.getInstance().getPermissionPool());
        String message = event.getMessage().replace("%", "%%");
        if (player.hasPermission("claytry.color")) {
            message = message.replace("°", "§");
        }
        event.setFormat(group.getDisplay() + player.getName() + " §8» §7" + message);
    }

    @EventHandler
    public void on(BukkitOnlineCountUpdateEvent event) {
        Bukkit.getOnlinePlayers().forEach(o -> {
            o.setLevel(event.getOnlineCount());
            o.setExp((float) event.getOnlineCount() / (float) 150);
        });
    }

    @EventHandler
    public void on(EntityDamageEvent event) {
        event.setCancelled(true);
    }


    @EventHandler
    public void on(PlayerInteractEvent event) {
        event.setCancelled(true);
        Player player = event.getPlayer();
        if (event.getAction().name().contains("RIGHT")) {
            if (event.getItem() != null) {
                if (event.getItem().hasItemMeta()) {
                    if (event.getItem().getItemMeta().hasDisplayName()) {
                        if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§8» §2§lT§aeleporter §8┃ §7Rechtsklick")) {
                            openNavigator(player);
                            player.playSound(player.getLocation(), Sound.CHEST_OPEN, 10, 10);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void on(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void on(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void on(BlockBreakEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void on(BlockPlaceEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void on(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void on(PlayerPickupItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void on(PlayerAchievementAwardedEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void on(EntityExplodeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void on(EntitySpawnEvent event) {
        event.setCancelled(true);
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

    public static void sendPlayerToServer(JavaPlugin javaPlugin, Player player, String serverName) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(serverName);
        player.sendPluginMessage(javaPlugin, "BungeeCord", out.toByteArray());
    }

    public static void registerBungeeChannel(JavaPlugin javaPlugin, Server server) {
        server.getMessenger().registerOutgoingPluginChannel(javaPlugin, "BungeeCord");

    }
}
