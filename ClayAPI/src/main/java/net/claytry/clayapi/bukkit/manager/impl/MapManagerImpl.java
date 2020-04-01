/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.clayapi.bukkit.manager.impl;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 28.07.2018 / 18:39                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/

import java.util.Objects;
import java.util.concurrent.*;

import net.claytry.clayapi.bukkit.manager.MapManager;
import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.*;
import java.io.*;

public class MapManagerImpl implements MapManager
{
    private final JavaPlugin plugin;
    private final ExecutorService pool;
    private World defaultMap;

    public MapManagerImpl(final JavaPlugin plugin) {
        this.plugin = plugin;
        this.pool = Executors.newFixedThreadPool(1);
        this.defaultMap = plugin.getServer().getWorlds().get(0);
    }

    @Override
    public World loadMap(String map) {
        map = map.toLowerCase();
        final WorldCreator worldcreator = new WorldCreator(map);
        worldcreator.generateStructures(false);
        this.plugin.getServer().createWorld(worldcreator);
        final World w = this.plugin.getServer().getWorld(map);
        w.setThundering(false);
        w.setStorm(false);
        w.setTime(6000L);
        w.setGameRuleValue("doDaylightCyle", "false");
        w.setGameRuleValue("doMobSpawning", "false");
        this.plugin.getServer().getWorlds().add(w);
        return w;
    }

    @Override
    public World loadFlatMap(final String map) {
        final WorldCreator worldcreator = new WorldCreator(map);
        worldcreator.type(WorldType.FLAT);
        this.plugin.getServer().createWorld(worldcreator);
        final World w = this.plugin.getServer().getWorld(map);
        this.plugin.getServer().getWorlds().add(w);
        return w;
    }

    @Override
    public void unloadMap(final String map) {
        this.plugin.getServer().unloadWorld(map, true);
    }

    @Override
    public void deleteMap(final String map) {
        try {
            final File file = this.plugin.getServer().getWorld(map).getWorldFolder();
            this.unloadMap(map);
        }
        catch (Exception ex) {}
        this.delete(new File(map));
    }

    private void delete(final File file) {
        if (!file.isDirectory()) {
            file.delete();
        }
        else {
            for (final File f : Objects.requireNonNull(file.listFiles())) {
                this.delete(f);
            }
        }
    }

    private void copy(final File src, final File dst) {
        try {
            if (!src.isDirectory()) {
                dst.getParentFile().mkdirs();
                Files.copy(src.toPath(), dst.toPath(), new CopyOption[0]);
            }
            else {
                for (final File f : Objects.requireNonNull(src.listFiles())) {
                    this.copy(f, new File(dst, f.getName()));
                }
            }
        }
        catch (IOException ex) {}
    }

    @Override
    public World getDefaultMap() {
        return this.defaultMap;
    }

    @Override
    public World getMap(final String map) {
        return this.plugin.getServer().getWorld(map);
    }

    @Override
    public void setDefaultMap(final World world) {
        this.defaultMap = world;
    }
}
