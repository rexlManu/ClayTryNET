/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.lobbymodule.settings;

import com.arangodb.entity.BaseDocument;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.claytry.clayapi.both.Message;
import net.claytry.clayapi.both.user.UserDocument;
import net.claytry.clayapi.bukkit.bridge.BukkitPlayerBridge;
import net.claytry.clayapi.bukkit.manager.ItemBuilder;
import net.claytry.lobbymodule.LobbyModule;
import net.claytry.lobbymodule.playerhider.PlayerHider;
import net.claytry.lobbymodule.utils.LocationUtils;
import net.claytry.lobbymodule.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 29.07.2018 / 19:08                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/
@AllArgsConstructor
@Getter
public enum Setting {

    POSITIONSYNC("positionsync", new ItemBuilder(Material.NETHER_STAR).setDisplayname("§8» §2Position-Synchronization").build()) {
        @Override
        public void setFunction(Player player, ItemStack itemStack, SavableSetting savableSetting) {
            for (int i = 0; i < settingOptions(player).length; i++) {
                if (settingOptions(player)[i].equals(itemStack)) {
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 1f);
                    player.closeInventory();
                    savableSetting.setCurrentStack(itemStack);
                }
            }
        }

        @Override
        public void onLoad(Player player, SavableSetting savableSetting) {
            final BaseDocument document = Utils.getDocumentByPlayer(player);
            if (!document.getProperties().containsKey("location")) {
                document.addAttribute("location", LocationUtils.saveToString(LobbyModule.getLobbyModule().getModuleConfig().getJsonConfig().getLocation("Spawn")));
                Utils.saveDocument(player, document);
            } else {
                final Location location = LocationUtils.readFromString(document.getAttribute("location").toString());
                if (LobbyModule.getLobbyModule().getSettingManager().getSelectedOption(player, savableSetting.getSetting()).getItemMeta().getDisplayName().toLowerCase().contains("an")) {
                    player.teleport(location);
                }
            }
        }

        @Override
        public SavableSetting onSave(Player player, SavableSetting savableSetting) {
            final BaseDocument document = Utils.getDocumentByPlayer(player);
            document.updateAttribute("location", player.getLocation());
            Utils.saveDocument(player, document);
            return savableSetting;
        }
    },
    PLAYERHIDER("playerhider", new ItemBuilder(Material.BLAZE_ROD).setDisplayname("§8» §2Spielersicht").build()) {
        @Override
        public ItemStack[] settingOptions(Player player) {
            return new ItemStack[]{
                    new ItemBuilder(Material.STAINED_CLAY, 1, 5).setDisplayname("§8» §aAlle Spieler").build(),
                    new ItemBuilder(Material.STAINED_CLAY, 1, 10).setDisplayname("§8» §5VIP Spieler").build(),
                    new ItemBuilder(Material.STAINED_CLAY, 1, 8).setDisplayname("§8» §7Keine Spieler").build()
            };
        }

        @Override
        public void setFunction(Player player, ItemStack itemStack, SavableSetting savableSetting) {
            savableSetting.setCurrentStack(itemStack);
            final PlayerHider playerHider = LobbyModule.getLobbyModule().getPlayerHider();
            for (PlayerHider.Status status : PlayerHider.Status.values()) {
                if (status.getTitleForSupportSetting().startsWith(itemStack.getItemMeta().getDisplayName())) {
                    player.closeInventory();
                    playerHider.setPlayerHiderStatus(status, player);
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 1f);
                    return;
                }
            }
        }

        @Override
        public void onLoad(Player player, SavableSetting savableSetting) {
            final PlayerHider playerHider = LobbyModule.getLobbyModule().getPlayerHider();
            for (PlayerHider.Status status : PlayerHider.Status.values()) {
                if (savableSetting.getCurrentStack().getItemMeta().getDisplayName().equalsIgnoreCase(status.getTitleForSupportSetting())) {
                    playerHider.setPlayerHiderStatus(status, player);
                }
            }
        }
    };

    private String key;
    private ItemStack displayItem;

    public void onLoad(Player player, SavableSetting savableSetting) {

    }

    public SavableSetting onSave(Player player, SavableSetting savableSetting) {
        return savableSetting;
    }

    public ItemStack[] settingOptions(Player player) {
        return new ItemStack[]{
                new ItemBuilder(Material.STAINED_CLAY, 1, 13).setDisplayname("§8» §aAn").build(),
                new ItemBuilder(Material.STAINED_CLAY, 1, 14).setDisplayname("§8» §cAus").build()
        };
    }

    public void setFunction(Player player, ItemStack itemStack, SavableSetting savableSetting) {
        player.sendMessage(Message.generatePrefix("Lobby") + "§7Deine Einstellung " + itemStack.getItemMeta().getDisplayName());
    }
}
