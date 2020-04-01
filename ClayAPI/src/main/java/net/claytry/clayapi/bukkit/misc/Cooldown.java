/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.clayapi.bukkit.misc;

import lombok.Getter;
import lombok.Setter;
import net.claytry.clayapi.both.api.CalldownCallback;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 04.07.2018 / 19:09                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/

public final class Cooldown implements ICooldown {

    @Getter
    private final int startTime;
    @Getter
    @Setter
    private int sec;

    @Getter
    private boolean running;
    private final JavaPlugin javaPlugin;

    private BukkitRunnable bukkitRunnable;

    private final CalldownCallback callback;

    public Cooldown(final int startTime, final JavaPlugin javaPlugin, final CalldownCallback callback) {
        this.startTime = startTime;
        this.sec = startTime;
        this.javaPlugin = javaPlugin;
        this.callback = callback;
        this.interalize();
    }

    private void interalize() {
        this.bukkitRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (Cooldown.this.sec < 1) {
                    Cooldown.this.callback.stop();
                    Cooldown.this.stop();
                    return;
                }
                Cooldown.this.sec--;
                Cooldown.this.callback.tick(Cooldown.this.sec);
            }
        };
    }

    @Override
    public void start() {
        this.running = true;
        this.bukkitRunnable.runTaskTimer(this.javaPlugin, 0, 20);
    }

    @Override
    public void stop() {
        this.bukkitRunnable.cancel();
        this.running = false;
        this.sec = this.startTime;
    }


}
