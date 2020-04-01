/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.clayapi.both.user;

import com.arangodb.ArangoCollection;
import com.arangodb.entity.BaseDocument;
import de.dytanic.cloudnet.api.player.PlayerExecutorBridge;
import net.claytry.clayapi.ClayAPI;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 28.07.2018 / 17:28                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/

public class BukkitUser implements User {

    private UUID uuid;
    private String name;
    private Player player;

    public BukkitUser(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.player = Bukkit.getPlayer(name);
        final ArangoCollection collection = ClayAPI.getInstance().getArangoConnector().getDatabase().collection("players");
        if (!collection.documentExists(uuid.toString())) {
            final BaseDocument value = new BaseDocument(uuid.toString());
            value.addAttribute("name", name);
            value.addAttribute("ip", player.getAddress().getAddress().getHostAddress());
            value.addAttribute("rank", "spieler");
            collection.insertDocument(value);
        }
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void sendMessage(String message) {
        player.sendMessage(message);
    }

    @Override
    public UserDocument getUserDocument() {
        return new UserDocument(this);
    }

}
