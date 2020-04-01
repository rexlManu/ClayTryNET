/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.clayapi.bukkit.manager.impl;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 28.07.2018 / 18:50                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/

import java.util.*;

import net.claytry.clayapi.bukkit.bridge.BukkitPlayerBridge;
import net.claytry.clayapi.bukkit.entities.NickSession;
import net.claytry.clayapi.bukkit.entities.impl.NickSessionImpl;
import net.claytry.clayapi.bukkit.manager.NickManager;
import net.claytry.clayapi.bukkit.misc.EventListener;
import org.bukkit.event.player.*;
import org.bukkit.event.*;
import org.bukkit.entity.*;

import java.text.*;

import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;

public class NickManagerImpl implements NickManager {
    private final JavaPlugin plugin;
    private final String prefix = " §e§lNick §8\u2503 §r";
    private final Map<UUID, NickSession> sessions;

    public NickManagerImpl(final JavaPlugin plugin) {
        this.sessions = new HashMap<>();
        this.plugin = plugin;
        this.init();
    }

    private void init() {
        BukkitPlayerBridge.getInstance().getEventManager().registerEvent(PlayerQuitEvent.class, (EventListener<PlayerQuitEvent>) event -> {
            final Player player = event.getPlayer();
            if (this.getSessions().containsKey(player.getUniqueId())) {
                this.getSessions().remove(player.getUniqueId());
            }
        });
    }

    @Override
    public Map<UUID, NickSession> getSessions() {
        return this.sessions;
    }

    @Override
    public void nickPlayer(final Player player, final String name) {
        if (!this.sessions.containsKey(player.getUniqueId())) {
            this.sessions.put(player.getUniqueId(), new NickSessionImpl(this.plugin, player));
        }
        final NickSession session = this.sessions.get(player.getUniqueId());
        session.setName(name, true);
        player.sendMessage(MessageFormat.format(" §e§lNick §8\u2503 §r§7Du hast den Namen §e{0} §7erhalten!", session.getCurrentNick()));
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 10.0f, 10.0f);
    }

    @Override
    public void unnickPlayer(final Player player) {
        if (!this.sessions.containsKey(player.getUniqueId())) {
            return;
        }
        final NickSession session = this.sessions.get(player.getUniqueId());
        session.reset(false);
        player.sendMessage(" §e§lNick §8\u2503 §r§7Dein §eNickname §7wurde entfernt!");
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 10.0f, 10.0f);
    }
}

