/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.clayapi.bukkit.utils;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 28.07.2018 / 18:27                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/

import org.bukkit.*;
import java.util.logging.*;
import org.bukkit.entity.*;
import java.lang.reflect.*;

public class ReflectUtil
{
    public static Field modifiers;

    public static Class<?> getNMSClass(final String name) {
        final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(ReflectUtil.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static void sendPacket(final Object packet) {
        Bukkit.getOnlinePlayers().forEach(player -> sendPacket(player, packet));
    }

    public static void sendPacket(final Player player, final Object packet) {
        try {
            final Object playerHandle = player.getClass().getMethod("getHandle", (Class<?>[])new Class[0]).invoke(player, new Object[0]);
            final Object playerConnection = playerHandle.getClass().getField("playerConnection").get(playerHandle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        }
        catch (NoSuchMethodException | SecurityException | NoSuchFieldException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex3) {
            Logger.getLogger(ReflectUtil.class.getName()).log(Level.SEVERE, null, ex3);
        }
    }

    public static void setField(final Object object, final String name, final Object to) throws Exception {
        final Field field = object.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(object, to);
        field.setAccessible(false);
    }

    public static Field getField(final Class<?> clazz, final String name) {
        try {
            final Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            if (Modifier.isFinal(field.getModifiers())) {
                ReflectUtil.modifiers.set(field, field.getModifiers() & 0xFFFFFFEF);
            }
            return field;
        }
        catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ex3) {
            Logger.getLogger(ReflectUtil.class.getName()).log(Level.SEVERE, null, ex3);
            return null;
        }
    }

    public String getVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }

    static {
        ReflectUtil.modifiers = getField(Field.class, "modifiers");
    }
}

