/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.clayapi.bukkit.bridge;

import lombok.Getter;
import net.claytry.clayapi.both.bridge.Bridge;
import net.claytry.clayapi.bukkit.manager.*;
import net.claytry.clayapi.bukkit.manager.impl.*;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 28.07.2018 / 14:33                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/

public class BukkitPlayerBridge implements Bridge {

    @Getter
    private static BukkitPlayerBridge instance;
    private JavaPlugin javaPlugin;
    @Getter
    private EventManager eventManager;
    @Getter
    private MapManager mapManager;
    @Getter
    private NickManager nickManager;
    @Getter
    private CloudManager cloudManager;
    @Getter
    private UserManager userManager;


    public BukkitPlayerBridge(JavaPlugin javaPlugin) {
        instance = this;
        this.javaPlugin = javaPlugin;
        /*
        init managers
         */
        this.eventManager = new EventManagerImpl(javaPlugin);
        this.cloudManager = new CloudManagerImpl();
        this.nickManager = new NickManagerImpl(getJavaPlugin());
        this.mapManager = new MapManagerImpl(getJavaPlugin());
        this.userManager = new UserManagerImpl();
    }

    public void sync(Runnable runnable) {
        javaPluginNotNull();
        Bukkit.getScheduler().runTask(javaPlugin, runnable);
    }

    @Deprecated
    public void async(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(javaPlugin, runnable);
    }

    public BukkitRunnable createBukkitRunnable(Runnable runnable) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        };
    }

    public Server getServer() {
        return Bukkit.getServer();
    }

    public void runTask(BukkitRunnable bukkitRunnable) {
        bukkitRunnable.runTask(javaPlugin);
    }

    public void runTaskLater(BukkitRunnable bukkitRunnable, Long delay) {
        bukkitRunnable.runTaskLater(javaPlugin, delay);
    }

    public void runTaskTimer(BukkitRunnable bukkitRunnable, Long delay, Long period) {
        bukkitRunnable.runTaskTimer(javaPlugin, delay, period);
    }

    public JavaPlugin getJavaPlugin() {
        return javaPlugin;
    }

    private void javaPluginNotNull() {
        if (javaPlugin == null) throw new NullPointerException("JavaPlugin is null");
    }

    public void runTaskLaterAsync(int delay, Runnable runnable) {
        createBukkitRunnable(runnable).runTaskLaterAsynchronously(getJavaPlugin(), delay);
    }
}
