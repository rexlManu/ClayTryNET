/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.clayapi.both.user;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.api.player.PlayerExecutorBridge;
import de.dytanic.cloudnet.lib.player.CloudPlayer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.UUID;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 28.07.2018 / 17:25                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/

public interface User {

    UUID getUUID();

    String getName();

    UserDocument getUserDocument();

    default void sendMessage(String message) {
        getPlayerExecutorBridge().sendMessage(getCloudPlayer(), message);
    }

    default void sendActionbar(String message) {
        getPlayerExecutorBridge().sendActionbar(getCloudPlayer(), message);
    }

    default void kickPlayer(String reason) {
        getPlayerExecutorBridge().kickPlayer(getCloudPlayer(), reason);
    }

    default void sendPlayer(String server) {
        getPlayerExecutorBridge().sendPlayer(getCloudPlayer(), server);
    }

    default void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        getPlayerExecutorBridge().sendTitle(getCloudPlayer(), title, subtitle, fadeIn, stay, fadeOut);
    }

    default CloudPlayer getCloudPlayer() {
        return CloudAPI.getInstance().getOnlinePlayer(getUUID());
    }

    default PlayerExecutorBridge getPlayerExecutorBridge() {
        return new PlayerExecutorBridge();
    }

}
