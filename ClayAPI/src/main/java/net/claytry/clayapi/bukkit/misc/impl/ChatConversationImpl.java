/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.clayapi.bukkit.misc.impl;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 28.07.2018 / 19:09                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/

import net.claytry.clayapi.bukkit.bridge.BukkitPlayerBridge;
import net.claytry.clayapi.bukkit.misc.ChatConversation;
import java.util.*;

import net.claytry.clayapi.bukkit.misc.EventListener;
import org.bukkit.entity.*;
import org.bukkit.event.player.*;
import java.text.*;
import org.bukkit.event.*;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatConversationImpl implements ChatConversation
{
    private boolean enabled;
    private String keyword;
    private String prefix;
    private final JavaPlugin plugin;
    private final ArrayList<Player> players;

    public ChatConversationImpl(final JavaPlugin plugin) {
        this.enabled = false;
        this.prefix = "";
        this.players = new ArrayList<Player>();
        this.plugin = plugin;
        BukkitPlayerBridge.getInstance().getEventManager().registerEvent(AsyncPlayerChatEvent.class, (EventListener<AsyncPlayerChatEvent>) event -> {
            if (this.enabled) {
                if (this.players.contains(event.getPlayer())) {
                    if (this.keyword == null || event.getMessage().startsWith(this.keyword)) {
                        if (!event.isCancelled()) {
                            event.setCancelled(true);
                            this.players.forEach(p -> {
                                final String name;
                                String s;
                                if (p != null) {
                                    if (p.hasPermission("keinleben.nick.seealways")) {
                                        if (event.getPlayer().hasMetadata("nickname")) {
                                            s = event.getPlayer().getCustomName() + "(" + event.getPlayer().getName() + ")";
                                        }
                                        else {
                                            s = event.getPlayer().getCustomName();
                                        }
                                    }
                                    else {
                                        s = event.getPlayer().getCustomName();
                                    }
                                    name = s;
                                    if (this.keyword != null) {
                                        if (event.getMessage().contains(p.getName())) {
                                            p.sendMessage(MessageFormat.format("{0} {1} §8>>§r {2}", this.prefix, name, event.getMessage().replaceFirst(this.keyword, "").replaceAll(p.getName(), "§a" + p.getName() + "§r")));
                                        }
                                        else {
                                            p.sendMessage(MessageFormat.format("{0} {1} §8>>§r {2}", this.prefix, name, event.getMessage().replaceFirst(this.keyword, "")));
                                        }
                                    }
                                    else if (event.getMessage().contains(p.getName())) {
                                        p.sendMessage(MessageFormat.format("{0} {1} §8>>§r {2}", this.prefix, name, event.getMessage().replaceAll(p.getName(), "§a" + p.getName() + "§r")));
                                    }
                                    else {
                                        p.sendMessage(MessageFormat.format("{0} {1} §8>>§r {2}", this.prefix, name, event.getMessage()));
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    @Override
    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    @Override
    public ChatConversation addPlayer(final Player player) {
        this.players.add(player);
        return this;
    }

    @Override
    public ChatConversation removePlayer(final Player player) {
        this.players.remove(player);
        return this;
    }

    @Override
    public ChatConversation setKeyword(final String keyword) {
        this.keyword = keyword;
        return this;
    }

    @Override
    public ChatConversation setEnabled(final boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @Override
    public ChatConversation setPrefix(final String prefix) {
        this.prefix = prefix;
        return this;
    }
}
