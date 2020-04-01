/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.lobbymodule.settings;

import com.arangodb.entity.BaseDocument;
import net.claytry.clayapi.ClayAPI;
import net.claytry.clayapi.both.user.User;
import net.claytry.clayapi.both.user.UserDocument;
import net.claytry.clayapi.bukkit.bridge.BukkitPlayerBridge;
import net.claytry.clayapi.bukkit.manager.ItemBuilder;
import net.claytry.clayapi.bukkit.manager.UserManager;
import net.claytry.clayapi.bukkit.misc.EventListener;
import net.claytry.lobbymodule.inventory.ClickableInventoryType;
import net.claytry.lobbymodule.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 29.07.2018 / 19:18                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/

public class SettingManager {

    private Map<Player, List<SavableSetting>> settingMap;

    public SettingManager() {
        this.settingMap = new HashMap<>();
        initListener();
    }

    public void loadSettings(Player player) {
        settingMap.put(player, new ArrayList<>());
        final BaseDocument baseDocument = Utils.getDocumentByPlayer(player);
        for (Setting setting : Setting.values()) {
            final ItemStack[] itemStacks = setting.settingOptions(player);
            if (!baseDocument.getProperties().containsKey(setting.name())) {
                baseDocument.addAttribute(setting.name(), itemStacks[0].getItemMeta().getDisplayName());
            }
            boolean found = false;
            for (int i = 0; i < itemStacks.length; i++) {
                if (itemStacks[i].getItemMeta().getDisplayName().equalsIgnoreCase(baseDocument.getAttribute(setting.name()).toString())) {
                    final SavableSetting e = new SavableSetting(setting, itemStacks[i]);
                    settingMap.get(player).add(e);
                    setting.onLoad(player, e);
                    found = true;
                }
            }
            if (!found) {
                settingMap.get(player).add(new SavableSetting(setting, itemStacks[0]));
            }
        }

        Utils.saveDocument(player, baseDocument);
    }

    public ItemStack getSelectedOption(Player player, Setting setting) {
        for (SavableSetting savableSetting : settingMap.get(player)) {
            if (savableSetting.getSetting().equals(setting)) {
                return savableSetting.getCurrentStack();
            }
        }
        return null;
    }


    public void saveSettings(Player player) {
        final BaseDocument document = Utils.getDocumentByPlayer(player);
        settingMap.get(player).forEach(savableSetting -> {
            final Setting setting = savableSetting.getSetting();
            if(savableSetting.getCurrentStack() != null){
                document.updateAttribute(setting.name(), savableSetting.getCurrentStack().getItemMeta().getDisplayName());
            }
            setting.onSave(player, savableSetting);
        });
        Utils.saveDocument(player, document);
    }

    private void initListener() {
        BukkitPlayerBridge.getInstance().getEventManager().registerEvent(InventoryClickEvent.class, (EventListener<InventoryClickEvent>) event -> {
            if (event.getInventory() != null) {
                final Player player = (Player) event.getWhoClicked();
                if (event.getInventory().getTitle().equals(ClickableInventoryType.SETTINGS.getTitle())) {
                    event.setCancelled(true);

                    if (event.getInventory().getType() == InventoryType.BREWING) {
                        if (event.getCurrentItem() != null) {
                            final String displayName = event.getInventory().getItem(3).getItemMeta().getDisplayName();
                            final List<SavableSetting> savableSettings = settingMap.get(player);
                            for (SavableSetting savableSetting : savableSettings) {
                                if (savableSetting.getSetting().getDisplayItem().getItemMeta().getDisplayName().equals(displayName)) {
                                    savableSetting.getSetting().setFunction(player, event.getCurrentItem(), savableSetting);
                                    //                                    openSetting(savableSetting, player);
                                    return;
                                }
                            }
                        }
                    } else {
                        if (event.getCurrentItem() != null) {
                            final String displayName = event.getCurrentItem().getItemMeta().getDisplayName();
                            final List<SavableSetting> savableSettings = settingMap.get(player);
                            for (SavableSetting savableSetting : savableSettings) {
                                if (savableSetting.getSetting().getDisplayItem().getItemMeta().getDisplayName().equals(displayName)) {
                                    openSetting(savableSetting, player);
                                    player.playSound(player.getLocation(), Sound.CLICK, 1, 1);
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    public void openSettingsMenu(Player player) {
        final Inventory inventory = Bukkit.createInventory(null, 3 * 9, ClickableInventoryType.SETTINGS.getTitle());
        final ItemStack itemStack = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 7).setDisplayname("§r").build();
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, itemStack);
        }
        int i = 9;
        for (Setting setting : Setting.values()) {
            inventory.setItem(i, setting.getDisplayItem());
            i++;
        }
        player.openInventory(inventory);
    }

    public void openSetting(SavableSetting setting, Player player) {
        final Inventory inventory = Bukkit.createInventory(null, InventoryType.BREWING, ClickableInventoryType.SETTINGS.getTitle());
        inventory.setItem(3, setting.getSetting().getDisplayItem());
        final ItemStack[] itemStacks = setting.getSetting().settingOptions(player);
        for (int i = 0; i < itemStacks.length; i++) {
            ItemStack itemStack = itemStacks[i];
            if (setting.getCurrentStack().equals(itemStack)) {
                itemStack = new ItemBuilder(itemStack).addGlow().build();
            }
            inventory.setItem(i, itemStack);
        }
        player.openInventory(inventory);
    }
}
