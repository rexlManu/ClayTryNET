/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.lobbymodule.inventory;

import net.claytry.clayapi.bukkit.manager.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 30.07.2018 / 04:00                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/

public class InfoInventory extends ClickableInventory {
    public InfoInventory() {
        super(ClickableInventoryType.INFO.getTitle());
    }

    @Override
    public Inventory build(Player player) {
        final Inventory inventory = Bukkit.createInventory(null, InventoryType.BREWING, getTitle());
        inventory.setItem(0, new ItemBuilder(Material.BOOK_AND_QUILL, 1).setDisplayname("§8» §2Informationen").setLore(
                " §8» §aClayZ §8× §7"+0,
                " §8» §aLieblingsspiel §8× §7"+"CSGO",
                " §8» §aErster Login §8× §7"+"Morgen"
        ).build());
        inventory.setItem(1, new ItemBuilder(Material.SKULL_ITEM, 1, 3)
                .setSkullTexture("http://textures.minecraft.net/texture/25b3f2cfa0739c4e828316f39f90b05bc1f4ed27b1e35888511f558d4675").setDisplayname("§8» §2Socialmedia").build());
        inventory.setItem(2, new ItemBuilder(Material.PAPER, 1).setDisplayname("§8» §2Statistiken").setLore(
                " §8» §aAbgebaute Blöcke §8× §7"+0,
                " §8» §aPlatzierte Blöcke §8× §7"+0,
                " §8» §aTode §8× §7"+0,
                " §8» §aKills §8× §7"+0,
                " §8» §aSpielzeit §8× §7"+0
        ).build());
        inventory.setItem(3, new ItemBuilder(Material.SKULL_ITEM, 1, 3).setDisplayname("§8» §a" + player.getName()).setSkullOwner(player.getName().toLowerCase()).build());
        return inventory;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
    }
}
