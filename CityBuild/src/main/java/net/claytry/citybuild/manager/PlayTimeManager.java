/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.citybuild.manager;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoDatabase;
import com.arangodb.entity.BaseDocument;
import net.claytry.citybuild.CityBuild;
import net.claytry.citybuild.entities.PlayTime;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 29.07.2018 / 15:49                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/

public class PlayTimeManager extends BukkitRunnable {

    private Map<Player, PlayTime> playTimeMap;
    private ArangoCollection collection;

    private CityBuild lobby;

    public PlayTimeManager(CityBuild lobby) {
        this.lobby = lobby;
        this.playTimeMap = new HashMap<>();
        final ArangoDatabase database = lobby.getArangoConnector().getDatabase();
        try {
            database.createCollection("players");
        } catch (Exception ignored) {
        }
        this.collection = database.collection("players");

        runTaskTimerAsynchronously(lobby, 0, 20);
    }

    public PlayTime loadPlayTime(Player player) {
        final BaseDocument document = getDocumentByPlayer(player);
        final int seconds = getInterger(document, "seconds");
        final int minutes = getInterger(document, "minutes");
        final int hours = getInterger(document, "hours");
        final PlayTime playTime = new PlayTime(hours, minutes, seconds);
        playTimeMap.put(player, playTime);
        saveDocument(player, document);
        return playTime;
    }

    private void saveDocument(Player player, BaseDocument document) {
        collection.updateDocument(player.getUniqueId().toString(), document);
    }

    public void savePlayTime(Player player) {
        if (playTimeMap.containsKey(player)) {
            final PlayTime playTime = playTimeMap.get(player);
            final BaseDocument document = getDocumentByPlayer(player);
            document.updateAttribute("seconds", playTime.getSeconds());
            document.updateAttribute("minutes", playTime.getMinutes());
            document.updateAttribute("hours", playTime.getHours());
            saveDocument(player, document);
        }
    }

    private int getInterger(BaseDocument baseDocument, String key) {
        if (!baseDocument.getProperties().containsKey(key)) {
            baseDocument.addAttribute(key, 0);
        }
        return Integer.parseInt(baseDocument.getAttribute(key).toString());
    }

    private BaseDocument getDocumentByPlayer(Player player) {
        return collection.getDocument(player.getUniqueId().toString(), BaseDocument.class);
    }

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(o -> {
            if (playTimeMap.containsKey(o)) {
                final PlayTime playTime = playTimeMap.get(o);
                playTime.addSecond();
                lobby.getPlayerScoreboard().updatePlayTime(o, playTime);
            } else loadPlayTime(o);
        });
    }
}
