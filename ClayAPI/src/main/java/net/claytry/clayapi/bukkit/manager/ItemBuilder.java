/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.clayapi.bukkit.manager;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.claytry.clayapi.both.api.Builder;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 20.05.2018 / 14:35                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/

public final class ItemBuilder implements Builder<ItemStack> {

    private final ItemStack itemStack;

    public ItemBuilder(final Material material, final int amount, final int data) {
        this.itemStack = new ItemStack(material, amount, (short) data);
    }

    public ItemBuilder(final Material material, final int amount) {
        this(material, amount, 0);
    }

    public ItemBuilder(final Material material) {
        this(material, 1);
    }

    public ItemBuilder(final ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemBuilder(final String name) {
        this(Material.SKULL_ITEM, 1, 3);
        if (!name.equals("")) {
            this.setSkullOwner(name.toLowerCase());
        }
    }

    public ItemBuilder setDisplayname(final String name) {
        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setAmount(final int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder setDurability(final short durability) {
        this.itemStack.setDurability(durability);
        return this;
    }

    public ItemBuilder setLore(final List<String> lore) {
        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.setLore(lore);
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setLore(final String... lore) {
        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.setLore(Arrays.asList(lore));
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addEnchant(final Enchantment enchantment, final int level, final boolean ignoreLevelRestriction) {
        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.addEnchant(enchantment, level, ignoreLevelRestriction);
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addEnchant(final Enchantment enchantment, final int level) {
        this.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder addEnchant(final Enchantment enchantment) {
        this.addEnchant(enchantment, 1, true);
        return this;
    }

    public ItemBuilder removeEnchant(final Enchantment enchantment) {
        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.removeEnchant(enchantment);
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addItemFlags(final ItemFlag... itemFlags) {
        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.addItemFlags(itemFlags);
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder removeItemFlags(final ItemFlag... itemFlags) {
        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.removeItemFlags(itemFlags);
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setSkullOwner(final String name) {
        final SkullMeta itemMeta = (SkullMeta) this.itemStack.getItemMeta();
        itemMeta.setOwner(name);
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    @Override
    public ItemStack build() {
        return this.itemStack;
    }

    /*
        ItemBuilder Utils Methods
     */

    public ItemBuilder addGlow() {
        this.addEnchant(Enchantment.DURABILITY);
        this.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemBuilder setUnbreakable(final boolean unbreakable) {
        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.spigot().setUnbreakable(unbreakable);
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder removeEnchants() {
        this.itemStack.getEnchantments().forEach((enchantment, integer) -> {
            this.removeEnchant(enchantment);
        });
        return this;
    }

    public ItemBuilder setSkullTexture(final String url) {
        final SkullMeta skullMeta = (SkullMeta) this.itemStack.getItemMeta();
//        final GameProfile profile = new GameProfile(UUID.randomUUID(), null);
//        final byte[] encodedData = org.apache.commons.codec.binary.Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
//        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
//        final Field profileField;
//        try {
//            profileField = meta.getClass().getDeclaredField("profile");
//            profileField.setAccessible(true);
//            profileField.set(meta, profile);
//        } catch (final NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
//            e1.printStackTrace();
//        }
        final GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        final byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            profileField = skullMeta.getClass().getDeclaredField("profile");
        } catch (final NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        profileField.setAccessible(true);
        try {
            profileField.set(skullMeta, profile);
        } catch (final IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        this.itemStack.setItemMeta(skullMeta);
        return this;
    }
}
