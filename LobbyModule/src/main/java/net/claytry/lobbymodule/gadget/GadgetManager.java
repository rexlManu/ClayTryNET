/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.lobbymodule.gadget;

import net.claytry.clayapi.bukkit.bridge.BukkitPlayerBridge;
import net.claytry.clayapi.bukkit.misc.EventListener;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 28.07.2018 / 22:29                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/

public class GadgetManager {

    private Set<Gadget> gadgets;

    public GadgetManager() {
        this.gadgets = new HashSet<>();
        initListener();
    }

    public void registerGadget(Gadget gadget) {
        this.gadgets.add(gadget);
    }

    public void setGadgets(Player player) {
        for (Gadget gadget : gadgets) {
            if (gadget.getPermission().isEmpty() || player.hasPermission(gadget.getPermission()))
                player.getInventory().setItem(gadget.getSlot(), gadget.build(player));
        }
    }

    private void initListener() {
        BukkitPlayerBridge.getInstance().getEventManager().registerEvent(PlayerInteractEvent.class, (EventListener<PlayerInteractEvent>) event -> {
            if (event.getItem() == null) return;
            for (Gadget gadget : gadgets) {
                if (event.getItem().equals(gadget.build(event.getPlayer()))) {
                    if (event.getPlayer().hasPermission(gadget.getPermission()) || gadget.getPermission().isEmpty())
                        gadget.onInteract(event);
                }
            }
        });
    }
}
