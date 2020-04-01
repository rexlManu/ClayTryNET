/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.clayapi.bukkit;

import net.claytry.clayapi.ClayAPI;
import net.claytry.clayapi.both.user.BukkitUser;
import net.claytry.clayapi.bukkit.bridge.BukkitPlayerBridge;
import net.claytry.clayapi.bukkit.misc.EventListener;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 28.07.2018 / 16:02                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/

public class BukkitClayAPI extends JavaPlugin {

    private ClayAPI clayAPI;
    private BukkitPlayerBridge bridge;

    @Override
    public void onEnable() {
        this.clayAPI = new ClayAPI();
        this.bridge = new BukkitPlayerBridge(this);
        this.clayAPI.onEnable();

        initEvents();
        this.clayAPI.getModuleManager().enableModules();
    }

    private void initEvents() {
        this.bridge.getEventManager().registerEvent(PlayerJoinEvent.class, (EventListener<PlayerJoinEvent>) event -> {
            final Player player = event.getPlayer();
            bridge.getUserManager().getOnlineUsers().add(new BukkitUser(player.getUniqueId(), player.getName()));
        });
        this.bridge.getEventManager().registerEvent(PlayerQuitEvent.class, (EventListener<PlayerQuitEvent>) event -> {
            final Player player = event.getPlayer();
            bridge.getUserManager().getOnlineUsers().remove(bridge.getUserManager().getUser(player.getUniqueId()));
        });
    }

    @Override
    public void onDisable() {
        this.clayAPI.onDisable();
    }
}
