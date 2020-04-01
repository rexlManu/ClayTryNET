/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.lobbymodule.gadget;

import net.claytry.clayapi.bukkit.manager.ItemBuilder;
import net.claytry.lobbymodule.LobbyModule;
import net.claytry.lobbymodule.inventory.ClickableInventoryType;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 29.07.2018 / 13:47                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/

public class PlayerhiderGadget extends Gadget {
    public PlayerhiderGadget() {
        super("", 4);
    }

    @Override
    public ItemStack build(Player player) {
        return new ItemBuilder(Material.BLAZE_ROD, 1).setDisplayname("§8» §2§lS§apielerverstecker §8┃ §7Rechtsklick").build();
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        event.setCancelled(true);
        if (event.getAction().name().contains("RIGHT")) {
            final Player player = event.getPlayer();
            player.playSound(player.getLocation(), Sound.CHEST_OPEN, 10, 10);
            LobbyModule.getLobbyModule().getInventoryManager().openInventory(player, ClickableInventoryType.PLAYERHIDER);
        }
    }
}
