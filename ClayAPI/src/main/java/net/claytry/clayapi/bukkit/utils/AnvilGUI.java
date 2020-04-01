/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.clayapi.bukkit.utils;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 10.06.2018 / 12:56                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/

public class AnvilGUI {
    private class AnvilContainer extends ContainerAnvil {
        public AnvilContainer(final EntityHuman entity) {
            super(entity.inventory, entity.world, new BlockPosition(0, 0, 0), entity);
        }

        @Override
        public boolean a(final EntityHuman entityhuman) {
            return true;
        }
    }

    public enum AnvilSlot {
        INPUT_LEFT(0),
        INPUT_RIGHT(1),
        OUTPUT(2);

        private final int slot;

        private AnvilSlot(final int slot) {
            this.slot = slot;
        }

        public int getSlot() {
            return this.slot;
        }

        public static AnvilSlot bySlot(final int slot) {
            for (final AnvilSlot anvilSlot : AnvilSlot.values()) {
                if (anvilSlot.getSlot() == slot) {
                    return anvilSlot;
                }
            }

            return null;
        }
    }

    public class AnvilClickEvent {
        private final AnvilSlot slot;

        private final String name;

        private boolean close = true;
        private boolean destroy = true;

        public AnvilClickEvent(final AnvilSlot slot, final String name) {
            this.slot = slot;
            this.name = name;
        }

        public AnvilSlot getSlot() {
            return this.slot;
        }

        public String getName() {
            return this.name;
        }

        public boolean getWillClose() {
            return this.close;
        }

        public void setWillClose(final boolean close) {
            this.close = close;
        }

        public boolean getWillDestroy() {
            return this.destroy;
        }

        public void setWillDestroy(final boolean destroy) {
            this.destroy = destroy;
        }
    }

    public interface AnvilClickEventHandler {
        public void onAnvilClick(AnvilClickEvent event);
    }

    private Player player;

    private AnvilClickEventHandler handler;

    private HashMap<AnvilSlot, ItemStack> items = new HashMap<AnvilSlot, ItemStack>();

    private Inventory inv;

    private Listener listener;

    public AnvilGUI(final Player player, final AnvilClickEventHandler handler, final JavaPlugin javaPlugin) {
        this.player = player;
        this.handler = handler;

        this.listener = new Listener() {
            @EventHandler
            public void onInventoryClick(final InventoryClickEvent event) {
                if (event.getWhoClicked() instanceof Player) {
                    final Player clicker = (Player) event.getWhoClicked();

                    if (event.getInventory().equals(AnvilGUI.this.inv)) {
                        event.setCancelled(true);

                        final ItemStack item = event.getCurrentItem();
                        final int slot = event.getRawSlot();
                        String name = "";

                        if (item != null) {
                            if (item.hasItemMeta()) {
                                final ItemMeta meta = item.getItemMeta();

                                if (meta.hasDisplayName()) {
                                    name = meta.getDisplayName();
                                }
                            }
                        }

                        final AnvilClickEvent clickEvent = new AnvilClickEvent(AnvilSlot.bySlot(slot), name);

                        handler.onAnvilClick(clickEvent);

                        if (clickEvent.getWillClose()) {
                            event.getWhoClicked().closeInventory();
                        }

                        if (clickEvent.getWillDestroy()) {
                            AnvilGUI.this.destroy();
                        }
                    }
                }
            }

            @EventHandler
            public void onInventoryClose(final InventoryCloseEvent event) {
                if (event.getPlayer() instanceof Player) {
                    final Player player = (Player) event.getPlayer();
                    final Inventory inv = event.getInventory();

                    if (inv.equals(AnvilGUI.this.inv)) {
                        inv.clear();
                        AnvilGUI.this.destroy();
                    }
                }
            }

            @EventHandler
            public void onPlayerQuit(final PlayerQuitEvent event) {
                if (event.getPlayer().equals(AnvilGUI.this.getPlayer())) {
                    AnvilGUI.this.destroy();
                }
            }
        };

        Bukkit.getPluginManager().registerEvents(this.listener, javaPlugin); //Replace with instance of main class
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setSlot(final AnvilSlot slot, final ItemStack item) {
        this.items.put(slot, item);
    }

    public void open() {
        final EntityPlayer p = ((CraftPlayer) this.player).getHandle();

        final AnvilContainer container = new AnvilContainer(p);

        //Set the items to the items from the inventory given
        this.inv = container.getBukkitView().getTopInventory();

        for (final AnvilSlot slot : this.items.keySet()) {
            this.inv.setItem(slot.getSlot(), this.items.get(slot));
        }

        //Counter stuff that the game uses to keep track of inventories
        final int c = p.nextContainerCounter();

        //Send the packet
        p.playerConnection.sendPacket(new PacketPlayOutOpenWindow(c, "minecraft:anvil", new ChatMessage("Repairing", new Object[]{}), 0));

        //Set their active container to the container
        p.activeContainer = container;

        //Set their active container window id to that counter stuff
        p.activeContainer.windowId = c;

        //Add the slot listener
        p.activeContainer.addSlotListener(p);
    }

    public void destroy() {
        this.player = null;
        this.handler = null;
        this.items = null;

        HandlerList.unregisterAll(this.listener);

        this.listener = null;
    }
}