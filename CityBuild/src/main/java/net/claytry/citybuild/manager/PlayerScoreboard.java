/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.citybuild.manager;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.lib.player.CloudPlayer;
import de.dytanic.cloudnet.lib.player.permission.PermissionGroup;
import net.claytry.citybuild.CityBuild;
import net.claytry.citybuild.entities.PlayTime;
import net.claytry.citybuild.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 29.07.2018 / 14:36                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/

public class PlayerScoreboard extends BukkitRunnable {

    private final String scoreboardTitle;
    private int animationTick;
    private boolean reserve;
    private CityBuild lobby;

    public PlayerScoreboard(CityBuild lobby) {
        this.lobby = lobby;
        runTaskTimer(lobby, 0, 100);
        this.scoreboardTitle = "§8» §2§lC§alayTry §8┃ §7CB";
        this.animationTick = 0;
        this.reserve = false;
    }

    @Override
    public void run() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (animationTick == -1) {
                    reserve = true;
                    animationTick = 0;
                }
                final String title = animateTitle();
                if (title == null) {
                    Bukkit.getOnlinePlayers().forEach(o -> {
                        if (o.getScoreboard() != null) {
                            if (o.getScoreboard().getObjective("lobby") != null) {
                                o.getScoreboard().getObjective("lobby").setDisplayName(scoreboardTitle);
                            }
                        }
                    });
                    animationTick = 0;
                    cancel();
                    return;
                }
                Bukkit.getOnlinePlayers().forEach(o -> {
                    if (o.getScoreboard() != null) {
                        if (o.getScoreboard().getObjective("lobby") != null) {
                            o.getScoreboard().getObjective("lobby").setDisplayName(title);
                        }
                    }
                });
                if (reserve) {
                    animationTick--;
                } else
                    animationTick++;
            }
        }.runTaskTimer(lobby, 0, 2);
    }

    public void updatePlayTime(Player player, PlayTime playTime) {
        if(player.getScoreboard().getTeam("playtime") != null){
            player.getScoreboard().getTeam("playtime").setSuffix("§7" + converToZahl(playTime.getHours()) + ":" + converToZahl(playTime.getMinutes()) + ":" + converToZahl(playTime.getSeconds()));
            player.setScoreboard(player.getScoreboard());
        }
    }

    private String converToZahl(int unit) {
        return String.valueOf((unit + "").length() == 1 ? "0" + unit : unit);
    }

    public void updateMoney(Player player) {
        if(player.getScoreboard().getTeam("geld") != null){
            player.getScoreboard().getTeam("geld").setSuffix("§7" + Utils.formatDouble(lobby.getMoneyManager().getMoney(player.getUniqueId())));
            player.setScoreboard(player.getScoreboard());
        }
    }

    public void sendScoreboard(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        final Objective objective = scoreboard.registerNewObjective("lobby", "dummy");
        objective.setDisplayName(scoreboardTitle);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.getScore("§a").setScore(10);
        objective.getScore(" §2✦ §8┃ §aDein Geld").setScore(9);
//        objective.getScore("  §8» §7Admin").setScore(8);
        createTeam(8, "geld", "  §8» ", "§7Loading", objective, scoreboard, "§e");
        objective.getScore("§b").setScore(7);
        objective.getScore(" §2➲ §8┃ §aDeine Spielzeit").setScore(6);
//        objective.getScore("  §8» §732:20:59").setScore(5);
        createTeam(6, "playtime", "  §8» ", "§7Loading§a", objective, scoreboard, "§f");
        objective.getScore("§c").setScore(4);
        objective.getScore(" §2✆ §8┃ §aTeamspeak").setScore(3);
//        objective.getScore("  §8» §7ts.claytry.net").setScore(2);
        createTeam(2, "teamspeak", "  §8» ", "§7ts.claytry.net", objective, scoreboard, "§1");
        objective.getScore("§d").setScore(1);

        player.setScoreboard(scoreboard);

        updateMoney(player);
    }

    public void updatePlayTablist(Player player){
        final Scoreboard scoreboard = player.getScoreboard();

        CloudAPI.getInstance().getPermissionPool().getGroups().forEach((s, permissionGroup) -> {
            final String teamName = permissionGroup.getTagId() + "_" + s;
            if (scoreboard.getTeam(teamName) == null) {
                final Team team = scoreboard.registerNewTeam(teamName);
                team.setPrefix(permissionGroup.getPrefix());
            }
        });


        Bukkit.getOnlinePlayers().forEach(o -> {
            final CloudPlayer onlinePlayer = CloudAPI.getInstance().getOnlinePlayer(o.getUniqueId());
            final PermissionGroup permissionGroup = onlinePlayer.getPermissionEntity().getHighestPermissionGroup(CloudAPI.getInstance().getPermissionPool());
            final String teamName = permissionGroup.getTagId() + "_" + permissionGroup.getName();
            scoreboard.getTeam(teamName).addEntry(o.getName());
        });
        player.setScoreboard(scoreboard);
    }

    public void updateTablist() {

        Bukkit.getOnlinePlayers().forEach(player -> {
            final Scoreboard scoreboard = player.getScoreboard();

            CloudAPI.getInstance().getPermissionPool().getGroups().forEach((s, permissionGroup) -> {
                final String teamName = permissionGroup.getTagId() + "_" + s;
                if (scoreboard.getTeam(teamName) == null) {
                    final Team team = scoreboard.registerNewTeam(teamName);
                    team.setPrefix(permissionGroup.getPrefix());
                }
            });


            Bukkit.getOnlinePlayers().forEach(o -> {
                final CloudPlayer onlinePlayer = CloudAPI.getInstance().getOnlinePlayer(o.getUniqueId());
                final PermissionGroup permissionGroup = onlinePlayer.getPermissionEntity().getHighestPermissionGroup(CloudAPI.getInstance().getPermissionPool());
                final String teamName = permissionGroup.getTagId() + "_" + permissionGroup.getName();
                scoreboard.getTeam(teamName).addEntry(o.getName());
            });
            player.setScoreboard(scoreboard);
        });

    }

    private void createTeam(int score, String teamName, String prefix, String suffix, Objective objectiveo, Scoreboard scoreboard, String entry) {
        Team team = scoreboard.registerNewTeam(teamName);
        team.setPrefix(prefix);
        team.setSuffix(suffix);
        team.addEntry(entry);
        objectiveo.getScore(entry).setScore(score);
    }

    public String animateTitle() {
        String prefix = "§8» §a";
        String suffix = " §8┃ §7CB";
        String mid = "ClayTry";
        final String[] data = mid.split("");
        if (data.length == (animationTick - 1) || data.length == animationTick) {
            reserve = true;
            animationTick--;
        }
        data[animationTick] = "§2§l" + data[animationTick] + "§a";
        final StringBuilder builder = new StringBuilder();
        for (String datum : data) {
            builder.append(datum);
        }
        return prefix + builder.toString() + suffix;
    }
}
