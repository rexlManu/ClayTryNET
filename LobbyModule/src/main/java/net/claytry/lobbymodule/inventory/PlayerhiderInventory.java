/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.lobbymodule.inventory;

import net.claytry.clayapi.bukkit.manager.ItemBuilder;
import net.claytry.lobbymodule.LobbyModule;
import net.claytry.lobbymodule.playerhider.PlayerHider;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 29.07.2018 / 13:48                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/

public class PlayerhiderInventory extends ClickableInventory {
    public PlayerhiderInventory() {
        super(ClickableInventoryType.PLAYERHIDER.getTitle());
    }

    @Override
    public Inventory build(Player player) {
        final Inventory inventory = Bukkit.createInventory(null, InventoryType.BREWING, getTitle());
        inventory.setItem(0, new ItemBuilder(Material.INK_SACK, 1, 10).setDisplayname("§8» §aAlle Spieler anzeigen").build());
        inventory.setItem(1, new ItemBuilder(Material.INK_SACK, 1, 5).setDisplayname("§8» §5VIP Spieler anzeigen").build());
        inventory.setItem(2, new ItemBuilder(Material.INK_SACK, 1, 8).setDisplayname("§8» §7Alle Spieler verstecken").build());

        inventory.setItem(3, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 7).setDisplayname("§r").build());

        return inventory;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);

        final Player player = (Player) event.getWhoClicked();
        if (event.getCurrentItem() == null) return;
        if (!event.getCurrentItem().hasItemMeta()) return;
        for (PlayerHider.Status status : PlayerHider.Status.values()) {
            if(status.getItemStack().equals(event.getCurrentItem())){
                final PlayerHider playerHider = LobbyModule.getLobbyModule().getPlayerHider();
                playerHider.setPlayerHiderStatus(status, player);
                player.closeInventory();
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 1f);
            }
        }
    }
}
