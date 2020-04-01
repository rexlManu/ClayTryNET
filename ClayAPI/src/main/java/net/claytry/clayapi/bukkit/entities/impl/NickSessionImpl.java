/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.clayapi.bukkit.entities.impl;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 28.07.2018 / 18:55                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.PropertyMap;
import net.claytry.clayapi.bukkit.bridge.BukkitPlayerBridge;
import net.claytry.clayapi.bukkit.entities.NickSession;
import net.claytry.clayapi.bukkit.utils.DataUtil;
import net.claytry.clayapi.bukkit.utils.ReflectUtil;
import org.bukkit.entity.*;

import java.util.logging.*;
import java.util.*;

import org.bukkit.plugin.*;

import java.lang.reflect.*;

import org.bukkit.*;
import org.bukkit.permissions.*;
import org.bukkit.plugin.java.JavaPlugin;

public class NickSessionImpl extends ReflectUtil implements NickSession {
    private final JavaPlugin plugin;
    private final List<String> permissions;
    private final Field nameField;
    private final Player holder;
    private final String realName;
    private String teamName;
    private String currentNick;
    private boolean enableTab;

    public NickSessionImpl(final JavaPlugin plugin, final Player holder) {
        this.nameField = ReflectUtil.getField(GameProfile.class, "name");
        this.plugin = plugin;
        this.holder = holder;
        this.realName = holder.getName();
        this.permissions = new ArrayList<String>();
        this.currentNick = this.realName;
    }

    @Override
    public void setName(final String name, final boolean synchronously) {
        this.currentNick = name;
        this.destroyPlayer(synchronously);
        if (!synchronously) {
            BukkitPlayerBridge.getInstance().runTaskLaterAsync(10, () -> this.buildPlayer(synchronously));
            return;
        }
        try {
            Thread.sleep(100L);
        } catch (InterruptedException ex) {
            Logger.getLogger(NickSessionImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.buildPlayer(synchronously);
    }

    @Override
    public boolean isNicked() {
        return !this.currentNick.equals(this.realName);
    }

    @Override
    public void reset(final boolean synchronously) {
        if (!this.isNicked()) {
            return;
        }
        this.setName(this.realName, synchronously);
        if (this.enableTab) {
            this.clearTabStyle();
        }
        this.enableTab = false;
    }

    private void buildPlayer(final boolean synchronously) {
        try {
            final Object enumDifficulty = ReflectUtil.getNMSClass("EnumDifficulty").getMethod("getById", Integer.TYPE).invoke(null, 0);
            final Object worldType = ReflectUtil.getNMSClass("WorldType").getDeclaredField(this.holder.getWorld().getWorldType().toString()).get(null);
            Object enumGameMode = null;
            try {
                enumGameMode = Class.forName("net.minecraft.server." + this.getVersion() + ".EnumGamemode").getMethod("getById", Integer.TYPE).invoke(null, this.holder.getGameMode().getValue());
            } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex7) {
                enumGameMode = Class.forName("net.minecraft.server." + this.getVersion() + ".WorldSettings").getDeclaredClasses()[0].getMethod("valueOf", String.class).invoke(null, this.holder.getGameMode().toString());
            }
            final Constructor<?> respawnConstructor = ReflectUtil.getNMSClass("PacketPlayOutRespawn").getConstructor(Integer.TYPE, enumDifficulty.getClass(), worldType.getClass(), enumGameMode.getClass());
            final Object respawnPacket = respawnConstructor.newInstance(0, enumDifficulty, worldType, enumGameMode);
            Constructor<?> positionConstructor = null;
            Object positionPacket = null;
            try {
                positionConstructor = ReflectUtil.getNMSClass("PacketPlayOutPosition").getConstructor(Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE, Float.TYPE, Set.class, Integer.TYPE);
                positionPacket = positionConstructor.newInstance(this.holder.getLocation().getX(), this.holder.getLocation().getY(), this.holder.getLocation().getZ(), this.holder.getLocation().getYaw(), this.holder.getLocation().getPitch(), new HashSet(), 0);
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex8) {
                positionConstructor = ReflectUtil.getNMSClass("PacketPlayOutPosition").getConstructor(Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE, Float.TYPE, Set.class);
                positionPacket = positionConstructor.newInstance(this.holder.getLocation().getX(), this.holder.getLocation().getY(), this.holder.getLocation().getZ(), this.holder.getLocation().getYaw(), this.holder.getLocation().getPitch(), new HashSet());
            }
            ReflectUtil.sendPacket(this.holder, respawnPacket);
            ReflectUtil.sendPacket(this.holder, positionPacket);
            final Object entityPlayer = this.holder.getClass().getMethod("getHandle", (Class<?>[]) new Class[0]).invoke(this.holder, new Object[0]);
            final Object entityPlayerArray = Array.newInstance(entityPlayer.getClass(), 1);
            Array.set(entityPlayerArray, 0, entityPlayer);
            final Object enumAddPlayer = ReflectUtil.getNMSClass("PacketPlayOutPlayerInfo").getDeclaredClasses()[2].getField("ADD_PLAYER").get(null);
            final Constructor<?> tabaddConstructor = ReflectUtil.getNMSClass("PacketPlayOutPlayerInfo").getConstructor(enumAddPlayer.getClass(), entityPlayerArray.getClass());
            final Object tabaddPacket = tabaddConstructor.newInstance(enumAddPlayer, entityPlayerArray);
            final Constructor<?> spawnConstructor = ReflectUtil.getNMSClass("PacketPlayOutNamedEntitySpawn").getConstructor(ReflectUtil.getNMSClass("EntityHuman"));
            final Object spawnPacket = spawnConstructor.newInstance(entityPlayer);
            Bukkit.getOnlinePlayers().forEach(player -> {
                if (player == this.holder) {
                    ReflectUtil.sendPacket(player, tabaddPacket);
                } else {
                    ReflectUtil.sendPacket(this.holder, spawnPacket);
                }
                return;
            });
            this.showPlayer(synchronously);
            this.holder.updateInventory();
            if (!synchronously) {
                this.holder.getEffectivePermissions().clear();
                this.permissions.forEach(perm -> {
                    final PermissionAttachment attachment;
                    attachment = this.holder.addAttachment((Plugin) this.plugin);
                    attachment.setPermission(perm, true);
                });
            }
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException | ClassNotFoundException | InstantiationException | NegativeArraySizeException | ArrayIndexOutOfBoundsException ex9) {
        }
    }

    private void destroyPlayer(final boolean synchronously) {
        try {
            this.permissions.clear();
            this.holder.getEffectivePermissions().forEach(info -> this.permissions.add(info.getPermission()));
            this.hidePlayer(synchronously);
            final GameProfile targetProfile = DataUtil.getProfile(this.currentNick);
            if (targetProfile == null) {
                this.holder.sendMessage(ChatColor.RED + "An Error occourred!");
                this.holder.sendMessage(ChatColor.RED + "No Response from Mojang & Database is empty!");
                this.showPlayer(synchronously);
                return;
            }
            try {
                final Object entityPlayer = this.holder.getClass().getMethod("getHandle", (Class<?>[]) new Class[0]).invoke(this.holder, new Object[0]);
                final Object entityPlayerArray = Array.newInstance(entityPlayer.getClass(), 1);
                Array.set(entityPlayerArray, 0, entityPlayer);
                final Object playerProfile = this.holder.getClass().getMethod("getProfile", (Class<?>[]) new Class[0]).invoke(this.holder, new Object[0]);
                this.nameField.set(playerProfile, targetProfile.getName());
                this.holder.setCustomName(targetProfile.getName());
                this.holder.setDisplayName(targetProfile.getName());
                final PropertyMap playerProps = (PropertyMap) playerProfile.getClass().getMethod("getProperties", (Class<?>[]) new Class[0]).invoke(playerProfile, new Object[0]);
                playerProps.removeAll((Object) "textures");
                playerProps.putAll( "textures", (Iterable) targetProfile.getProperties().get( "textures"));
                final Constructor<?> destroyConstructor = ReflectUtil.getNMSClass("PacketPlayOutEntityDestroy").getConstructor(int[].class);
                final Object destroyPacket = destroyConstructor.newInstance(new int[]{(int) this.holder.getClass().getMethod("getEntityId", (Class<?>[]) new Class[0]).invoke(this.holder, new Object[0])});
                final Object enumRemovePlayer = ReflectUtil.getNMSClass("PacketPlayOutPlayerInfo").getDeclaredClasses()[2].getField("REMOVE_PLAYER").get(null);
                final Constructor<?> tabremoveConstructor = ReflectUtil.getNMSClass("PacketPlayOutPlayerInfo").getConstructor(enumRemovePlayer.getClass(), entityPlayerArray.getClass());
                final Object tabremovePacket = tabremoveConstructor.newInstance(enumRemovePlayer, entityPlayerArray);
                Bukkit.getOnlinePlayers().forEach(player -> {
                    if (player == this.holder) {
                        ReflectUtil.sendPacket(player, destroyPacket);
                    } else {
                        ReflectUtil.sendPacket(player, tabremovePacket);
                    }
                });
            } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NegativeArraySizeException | ArrayIndexOutOfBoundsException | InstantiationException ex3) {
                Logger.getLogger(NickSessionImpl.class.getName()).log(Level.SEVERE, null, ex3);
            }
        } catch (NoSuchMethodException | NoSuchFieldException  | ArrayIndexOutOfBoundsException | NegativeArraySizeException  | IllegalArgumentException  | SecurityException ex4) {
        }
    }

    private void showPlayer(final boolean synchronously) {
        if (synchronously) {
            Bukkit.getOnlinePlayers().forEach(player -> player.showPlayer(this.holder));
            return;
        }
        Bukkit.getOnlinePlayers().forEach(player -> player.showPlayer(this.holder));
    }

    private void hidePlayer(final boolean synchronously) {
        if (synchronously) {
            Bukkit.getOnlinePlayers().forEach(player -> player.hidePlayer(this.holder));
            return;
        }
        Bukkit.getOnlinePlayers().forEach(player -> player.hidePlayer(this.holder));
    }

    @Override
    public void setTabStyle(final String prefix, final String suffix, final int priority) {
        this.teamName = this.holder.getName();
        if (this.teamName.length() > 16) {
            this.teamName = this.teamName.substring(0, 16);
        }
        try {
            final Constructor<?> constructor = ReflectUtil.getNMSClass("PacketPlayOutScoreboardTeam").getConstructor((Class<?>[]) new Class[0]);
            final Object packet = constructor.newInstance(new Object[0]);
            final List<String> contents = new ArrayList<String>();
            contents.add(this.holder.getName());
            try {
                ReflectUtil.setField(packet, "a", this.teamName);
                ReflectUtil.setField(packet, "b", this.teamName);
                ReflectUtil.setField(packet, "c", prefix);
                ReflectUtil.setField(packet, "d", suffix);
                ReflectUtil.setField(packet, "e", "ALWAYS");
                ReflectUtil.setField(packet, "h", 0);
                ReflectUtil.setField(packet, "g", contents);
            } catch (Exception ex2) {
                ReflectUtil.setField(packet, "a", this.teamName);
                ReflectUtil.setField(packet, "b", this.teamName);
                ReflectUtil.setField(packet, "c", prefix);
                ReflectUtil.setField(packet, "d", suffix);
                ReflectUtil.setField(packet, "e", "ALWAYS");
                ReflectUtil.setField(packet, "i", 0);
                ReflectUtil.setField(packet, "h", contents);
            }
            ReflectUtil.sendPacket(packet);
        } catch (Exception ex) {
            Logger.getLogger(NickSessionImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void clearTabStyle() {
        try {
            final Constructor<?> constructor = ReflectUtil.getNMSClass("PacketPlayOutScoreboardTeam").getConstructor((Class<?>[]) new Class[0]);
            final Object packet = constructor.newInstance(new Object[0]);
            final List<String> contents = new ArrayList<String>();
            contents.add(this.holder.getName());
            try {
                ReflectUtil.setField(packet, "a", this.teamName);
                ReflectUtil.setField(packet, "b", this.teamName);
                ReflectUtil.setField(packet, "e", "ALWAYS");
                ReflectUtil.setField(packet, "h", 1);
                ReflectUtil.setField(packet, "g", contents);
            } catch (Exception ex2) {
                ReflectUtil.setField(packet, "a", this.teamName);
                ReflectUtil.setField(packet, "b", this.teamName);
                ReflectUtil.setField(packet, "e", "ALWAYS");
                ReflectUtil.setField(packet, "i", 1);
                ReflectUtil.setField(packet, "h", contents);
            }
            ReflectUtil.sendPacket(packet);
        } catch (Exception ex) {
            Logger.getLogger(NickSessionImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Player getHolder() {
        return this.holder;
    }

    @Override
    public String getRealName() {
        return this.realName;
    }

    @Override
    public String getCurrentNick() {
        return this.currentNick;
    }
}
