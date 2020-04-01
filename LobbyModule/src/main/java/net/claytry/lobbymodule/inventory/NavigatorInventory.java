/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.lobbymodule.inventory;

import net.claytry.clayapi.bukkit.bridge.BukkitPlayerBridge;
import net.claytry.clayapi.bukkit.manager.ItemBuilder;
import net.claytry.lobbymodule.LobbyModule;
import net.claytry.lobbymodule.utils.TitleAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.atomic.AtomicInteger;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 28.07.2018 / 22:52                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/

public class NavigatorInventory extends ClickableInventory {

    public NavigatorInventory() {
        super(ClickableInventoryType.NAVIGATOR.getTitle());
    }

    @Override
    public Inventory build(Player player) {
        final Inventory inventory = Bukkit.createInventory(null, 3 * 9, getTitle());
        final ItemStack itemStack = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 7).setDisplayname("§r").build();
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, itemStack);
        }


        AtomicInteger atomicInteger = new AtomicInteger();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!inventory.getViewers().contains(player)) {
                    cancel();
                    return;
                }
                switch (atomicInteger.get()) {
                    case 1:
                        player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 10);
                        inventory.setItem(9, new ItemBuilder(Material.IRON_PICKAXE, 1).setDisplayname("§8» §2Citybuild").build());
                        inventory.setItem(17, new ItemBuilder(Material.IRON_FENCE, 1).setDisplayname("§8» §2Coming soon").build());
                        break;
                    case 2:
                        player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 10);
                        inventory.setItem(9, itemStack);
                        inventory.setItem(17, itemStack);
                        inventory.setItem(10, new ItemBuilder(Material.IRON_PICKAXE, 1).setDisplayname("§8» §2Citybuild").build());
                        inventory.setItem(16, new ItemBuilder(Material.IRON_FENCE, 1).setDisplayname("§8» §2Coming soon").build());
                        break;
                    case 3:
                        player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 10);
                        inventory.setItem(2 + 9 + 9, new ItemBuilder(Material.GRASS, 1).setDisplayname("§8» §2SkyWars").build());
                        inventory.setItem(6, new ItemBuilder(Material.FEATHER, 1).setDisplayname("§8» §2DropIt").build());
                        break;
                    case 5:
                        player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 10);
                        inventory.setItem(2 + 9 + 9, itemStack);
                        inventory.setItem(6, itemStack);
                        inventory.setItem(2 + 9, new ItemBuilder(Material.GRASS, 1).setDisplayname("§8» §2SkyWars").build());
                        inventory.setItem(6 + 9, new ItemBuilder(Material.FEATHER, 1).setDisplayname("§8» §2DropIt").build());
                        break;
                    case 7:
                        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 5);
                        inventory.setItem(4 + 9, new ItemBuilder(Material.NETHER_STAR, 1).setDisplayname("§8» §2Spawn").build());
                        break;
                    case 8:
                        cancel();
                        break;

                }
                atomicInteger.addAndGet(1);
            }
        }.runTaskTimer(BukkitPlayerBridge.getInstance().getJavaPlugin(), 5, 3);
        return inventory;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        if (event.getCurrentItem() == null) return;
        if(!event.getCurrentItem().hasItemMeta()) return;
        if(!event.getCurrentItem().getItemMeta().hasDisplayName()) return;
        final String displayName = event.getCurrentItem().getItemMeta().getDisplayName();
        final Player player = (Player) event.getWhoClicked();
        final String warp = displayName.replace("§8» §a", "");
        final Location location = LobbyModule.getLobbyModule().getModuleConfig().getJsonConfig().getLocation(warp);
        if (location != null) {
            player.teleport(location);
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 1f);
            TitleAPI.sendTitle(player, 5, 20, 5, "§8» §2§lC§alayTry.net", "§8– §a" + warp + " §8–");
        }
    }
}
