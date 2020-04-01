/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.clayapi.bukkit.utils;

import net.claytry.clayapi.both.api.Callback;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 05.07.2018 / 16:59                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/

public final class PlayerUtils {

    /**
     * Goes though all players.
     *
     * @param playerCallback
     */
    public static void parseThroughAllPlayer(final Callback<Player> playerCallback) {
        Bukkit.getOnlinePlayers().forEach(playerCallback::call);
    }
}
