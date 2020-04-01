/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.clayapi.bukkit.manager.impl;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 28.07.2018 / 18:35                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/

import net.claytry.clayapi.bukkit.manager.EventManager;
import net.claytry.clayapi.bukkit.misc.EventListener;
import net.claytry.clayapi.bukkit.misc.ListenerExecutor;
import org.bukkit.plugin.*;
import org.bukkit.event.player.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.command.*;
import org.bukkit.event.*;
import org.bukkit.plugin.java.JavaPlugin;

public class EventManagerImpl implements EventManager, Listener {

    private final JavaPlugin plugin;
    private final HashMap<EventListener, CopyOnWriteArrayList<ListenerExecutor>> executors;
    private final HashMap<String, CommandExecutor> commands;

    public EventManagerImpl(final JavaPlugin plugin) {
        this.executors = new HashMap<>();
        this.commands = new HashMap<>();
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void registerEvent(final Class<? extends Event> cls, final EventListener listener) {
        final ListenerExecutor executor = new ListenerExecutor(cls, listener);

        this.plugin.getServer().getPluginManager().registerEvent(cls, new Listener() {
        }, EventPriority.NORMAL, executor, this.plugin);
        if (!this.executors.containsKey(listener)) {
            this.executors.put(listener, new CopyOnWriteArrayList<>());
        }
        this.executors.get(listener).add(executor);
    }

    @Override
    public void unregisterEvent(final Class<? extends Event> cls, final EventListener listener) {
        if (this.executors.containsKey(listener)) {
            this.executors.get(listener).stream().filter(executor -> executor.getListener().equals(listener)).forEach(executor -> executor.setDisable(true));
        }
    }

    @Override
    public void onCommand(final String command, final CommandExecutor commandExecutor) {
        this.commands.put(command.toLowerCase(), commandExecutor);
    }

    @EventHandler
    public void onCommand(final PlayerCommandPreprocessEvent event) {
        final ArrayList<String> args = new ArrayList<>(Arrays.asList(event.getMessage().replaceFirst("/", "").split(" ")));
        if (!this.commands.containsKey(args.get(0).toLowerCase())) {
            return;
        }
        final CommandExecutor executor = this.commands.get(args.get(0).toLowerCase());
        args.remove(0);
        executor.onCommand(event.getPlayer(), null, null, args.toArray(new String[0]));
        event.setCancelled(true);
    }
}
