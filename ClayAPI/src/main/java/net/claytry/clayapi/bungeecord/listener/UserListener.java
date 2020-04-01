/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.clayapi.bungeecord.listener;

import net.claytry.clayapi.both.user.ProxyUser;
import net.claytry.clayapi.bukkit.manager.UserManager;
import net.claytry.clayapi.bungeecord.bridge.BungeePlayerBridge;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 28.07.2018 / 20:33                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/

public class UserListener extends Listener {

    public UserListener(Plugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void on(PostLoginEvent event) {
        BungeePlayerBridge.getInstance().getUserManager().getOnlineUsers().add(new ProxyUser(event.getPlayer().getUniqueId(), event.getPlayer().getName()));
    }

    @EventHandler
    public void on(ServerDisconnectEvent event) {
        final UserManager userManager = BungeePlayerBridge.getInstance().getUserManager();
        userManager.getOnlineUsers().remove(userManager.getUser(event.getPlayer().getUniqueId()));
    }
}
