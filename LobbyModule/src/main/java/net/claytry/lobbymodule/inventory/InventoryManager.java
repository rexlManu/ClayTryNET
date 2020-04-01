/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.lobbymodule.inventory;

import net.claytry.clayapi.bukkit.bridge.BukkitPlayerBridge;
import net.claytry.clayapi.bukkit.misc.EventListener;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashSet;
import java.util.Set;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 28.07.2018 / 22:44                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/

public class InventoryManager {

    private Set<ClickableInventory> clickableInventories;

    public InventoryManager() {
        this.clickableInventories = new HashSet<>();
        initListener();
    }

    public void registerInventory(ClickableInventory inventory) {
        this.clickableInventories.add(inventory);
    }

    public void openInventory(Player player, ClickableInventoryType type) {
        for (ClickableInventory inventory : clickableInventories) {
            if (inventory.getTitle().equals(type.getTitle())) {
                player.openInventory(inventory.build(player));
            }
        }
    }

    public void openInventory(Player player, ClickableInventoryType type, Player target) {
        for (ClickableInventory inventory : clickableInventories) {
            if (inventory.getTitle().equals(type.getTitle())) {
                player.openInventory(inventory.build(target));
            }
        }
    }

    private void initListener() {
        BukkitPlayerBridge.getInstance().getEventManager().registerEvent(InventoryClickEvent.class, (EventListener<InventoryClickEvent>) event -> {
            if (event.getInventory() == null)
                return;

            for (ClickableInventory inventory : clickableInventories) {
                if (event.getInventory().getTitle().equals(inventory.getTitle())) {
                    inventory.onClick(event);
                }
            }

        });
    }
}
