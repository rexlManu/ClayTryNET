/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.clayapi.bungeecord;

import net.claytry.clayapi.ClayAPI;
import net.claytry.clayapi.both.bridge.Bridge;
import net.claytry.clayapi.bukkit.misc.EventListener;
import net.claytry.clayapi.bungeecord.bridge.BungeePlayerBridge;
import net.claytry.clayapi.bungeecord.listener.UserListener;
import net.md_5.bungee.api.plugin.Plugin;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 28.07.2018 / 20:31                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/

public class BungeeClayAPI extends Plugin {

    private ClayAPI clayAPI;
    private Bridge bridge;

    @Override
    public void onEnable() {
        this.clayAPI = new ClayAPI();
        this.clayAPI.onEnable();
        this.bridge = new BungeePlayerBridge(this);
        new UserListener(this);
        this.clayAPI.getModuleManager().enableModules();
    }

    @Override
    public void onDisable() {
        this.clayAPI.onDisable();
    }
}
