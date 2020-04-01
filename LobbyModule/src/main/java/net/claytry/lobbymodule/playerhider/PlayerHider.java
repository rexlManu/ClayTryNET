/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.lobbymodule.playerhider;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import net.claytry.clayapi.bukkit.manager.ItemBuilder;
import net.claytry.clayapi.bukkit.utils.PlayerUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 29.07.2018 / 14:07                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/

public class PlayerHider {

    @Getter
    private Map<UUID, Status> playerHiderStatus;

    public PlayerHider() {
        this.playerHiderStatus = new HashMap<>();
    }

    public Status switchStatus(Player player) {
        if (!playerHiderStatus.containsKey(player.getUniqueId())) {
            playerHiderStatus.put(player.getUniqueId(), Status.NONE);
        }
        Status status = playerHiderStatus.get(player.getUniqueId());
        switch (status) {
            case ALL:
                status = Status.VIP;
                break;
            case VIP:
                status = Status.NONE;
                break;
            case NONE:
                status = Status.ALL;
                break;
        }
        return status;
    }

    public Status getPlayerHiderStatus(Player player) {
        if (!playerHiderStatus.containsKey(player.getUniqueId())) {
            playerHiderStatus.put(player.getUniqueId(), Status.ALL);
        }
        return playerHiderStatus.get(player.getUniqueId());
    }

    public void setPlayerHiderStatus(Status status, Player player) {
        switch (status) {
            case NONE:
                PlayerUtils.parseThroughAllPlayer(player::hidePlayer);
                break;
            case VIP:
                PlayerUtils.parseThroughAllPlayer(o -> {
                    if (o.hasPermission("claytry.team")) {
                        player.showPlayer(o);
                    } else player.hidePlayer(o);
                });
                break;
            case ALL:
                PlayerUtils.parseThroughAllPlayer(player::showPlayer);
                break;
        }

        playerHiderStatus.put(player.getUniqueId(), status);
    }

    public enum Status {
        ALL(new ItemBuilder(Material.INK_SACK, 1, 10).setDisplayname("§8» §aAlle Spieler anzeigen").build(), "§8» §aAlle Spieler"),
        VIP(new ItemBuilder(Material.INK_SACK, 1, 5).setDisplayname("§8» §5VIP Spieler anzeigen").build(), "§8» §5VIP Spieler"),
        NONE(new ItemBuilder(Material.INK_SACK, 1, 8).setDisplayname("§8» §7Alle Spieler verstecken").build(), "§8» §7Keine Spieler");

        private ItemStack itemStack;
        private String titleForSupportSetting;

        Status(ItemStack itemStack, String titleForSupportSetting) {
            this.itemStack = itemStack;
            this.titleForSupportSetting = titleForSupportSetting;
        }

        public String getTitleForSupportSetting() {
            return titleForSupportSetting;
        }

        public ItemStack getItemStack() {
            return itemStack;
        }
    }
}
