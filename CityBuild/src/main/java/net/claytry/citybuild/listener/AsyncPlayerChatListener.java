package net.claytry.citybuild.listener;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.bridge.event.bukkit.BukkitOnlineCountUpdateEvent;
import de.dytanic.cloudnet.bridge.event.bukkit.BukkitPlayerUpdateEvent;
import de.dytanic.cloudnet.lib.player.permission.PermissionGroup;
import net.claytry.citybuild.CityBuild;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public final class AsyncPlayerChatListener implements Listener {

    private CityBuild cityBuild;

    public AsyncPlayerChatListener(CityBuild cityBuild) {
        this.cityBuild = cityBuild;
        Bukkit.getPluginManager().registerEvents(this, cityBuild);
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


}
