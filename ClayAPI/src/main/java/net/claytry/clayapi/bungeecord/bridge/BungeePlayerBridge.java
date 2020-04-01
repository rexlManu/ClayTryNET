/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.clayapi.bungeecord.bridge;

import lombok.Getter;
import net.claytry.clayapi.both.bridge.Bridge;
import net.claytry.clayapi.bukkit.bridge.BukkitPlayerBridge;
import net.claytry.clayapi.bukkit.manager.UserManager;
import net.claytry.clayapi.bukkit.manager.impl.UserManagerImpl;
import net.md_5.bungee.api.plugin.Plugin;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 28.07.2018 / 14:34                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/

public class BungeePlayerBridge implements Bridge {

    @Getter
    private static BungeePlayerBridge instance;
    @Getter
    private Plugin plugin;
    @Getter
    private UserManager userManager;

    public BungeePlayerBridge(Plugin plugin) {
        this.plugin = plugin;
        instance = this;
        this.userManager = new UserManagerImpl();
    }
}
