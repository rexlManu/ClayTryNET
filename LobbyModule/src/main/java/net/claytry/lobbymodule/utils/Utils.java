/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.lobbymodule.utils;

import com.arangodb.entity.BaseDocument;
import net.claytry.clayapi.ClayAPI;
import org.bukkit.entity.Player;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 30.07.2018 / 05:16                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/

public class Utils {

    public static BaseDocument getDocumentByPlayer(Player player) {
        return ClayAPI.getInstance().getArangoConnector().getDatabase().collection("players").getDocument(player.getUniqueId().toString(), BaseDocument.class);
    }

    public static void saveDocument(Player player, BaseDocument document) {
        ClayAPI.getInstance().getArangoConnector().getDatabase().collection("players").updateDocument(player.getUniqueId().toString(), document);
    }
}
