package net.claytry.citybuild.listener;

import net.claytry.citybuild.CityBuild;
import net.claytry.citybuild.entities.PlayTime;
import net.claytry.citybuild.manager.MoneyManager;
import net.claytry.citybuild.utils.TitleAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class PlayerJoinListener implements Listener {

    private CityBuild cityBuild;

    public PlayerJoinListener(CityBuild cityBuild) {
        this.cityBuild = cityBuild;
        Bukkit.getPluginManager().registerEvents(this, cityBuild);
    }

    @EventHandler
    public void on(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPlayedBefore()) {
            MoneyManager moneyManager = cityBuild.getMoneyManager();
            moneyManager.addMoney(player.getUniqueId(), 500.0);
            player.teleport((Location) cityBuild.getConfiguration().get("Spawn"));
        }
        event.setJoinMessage(cityBuild.getPrefix() + "§a" + player.getName() + "§7 hat den Server betreten.");
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 1f);
        PlayTime playTime = cityBuild.getPlayTimeManager().loadPlayTime(player);
        cityBuild.getPlayerScoreboard().sendScoreboard(player);
        cityBuild.getPlayerScoreboard().updateTablist();
        cityBuild.getPlayerScoreboard().updatePlayTime(player, playTime);
        TitleAPI.sendTitle(player, 10, 20, 10, "§8» §2§lC§alayTry.net", "§7Willkommen§8, §a" + player.getName());
    }


    @EventHandler
    public void on(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(cityBuild.getPrefix() + "§a" + player.getName() + "§7 hat den Server verlassen.");
        cityBuild.getPlayTimeManager().savePlayTime(player);
    }
}
