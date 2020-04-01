package cz.thezak.warps.utils;

import org.bukkit.entity.*;
import cz.thezak.warps.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.configuration.*;

import java.util.*;

public class Inv {
    private static ArrayList<ItemStack> warpitem;

    public void mainInv(final Player p) {
        final Inventory inv = Bukkit.createInventory((InventoryHolder) null, 27, "" + Warps.plugin.getConfig().getString("Messages.Menu").replaceAll("&", "§"));
        final ItemStack warpitem = new ItemStack(Material.valueOf("" + Warps.plugin.getConfig().getString("Icons.Warps").replaceAll("&", "§")));
        final ItemMeta warpmeta = warpitem.getItemMeta();
        warpmeta.setDisplayName("" + Warps.plugin.getConfig().getString("Messages.Warps").replaceAll("&", "§"));
        warpitem.setItemMeta(warpmeta);
        final ItemStack homeitem = new ItemStack(Material.valueOf("" + Warps.plugin.getConfig().getString("Icons.Homes").replaceAll("&", "§")));
        final ItemMeta homemeta = homeitem.getItemMeta();
        homemeta.setDisplayName("" + Warps.plugin.getConfig().getString("Messages.Homes").replaceAll("&", "§"));
        homeitem.setItemMeta(homemeta);
        final ItemStack spawnitem = new ItemStack(Material.valueOf("" + Warps.plugin.getConfig().getString("Icons.Spawn").replaceAll("&", "§")));
        final ItemMeta spawnmeta = spawnitem.getItemMeta();
        spawnmeta.setDisplayName("" + Warps.plugin.getConfig().getString("Messages.Spawn").replaceAll("&", "§"));
        spawnitem.setItemMeta(spawnmeta);
        final ItemStack watchitem = new ItemStack(Material.valueOf("" + Warps.plugin.getConfig().getString("Icons.ToggleWatchOn").replaceAll("&", "§")));
        final ItemMeta watchmeta = watchitem.getItemMeta();
        watchmeta.setDisplayName("" + Warps.plugin.getConfig().getString("Messages.ToggleMagicWatch").replaceAll("&", "§"));
        watchitem.setItemMeta(watchmeta);
        final ItemStack paperitem = new ItemStack(Material.valueOf("" + Warps.plugin.getConfig().getString("Icons.ToggleWatchOff").replaceAll("&", "§")));
        final ItemMeta papermeta = paperitem.getItemMeta();
        papermeta.setDisplayName("" + Warps.plugin.getConfig().getString("Messages.ToggleMagicWatch").replaceAll("&", "§"));
        paperitem.setItemMeta(papermeta);
        final ItemStack nopermitem = new ItemStack(Material.valueOf("" + Warps.plugin.getConfig().getString("Icons.NoPermissions").replaceAll("&", "§")));
        final ItemMeta nopermmeta = nopermitem.getItemMeta();
        nopermmeta.setDisplayName("" + Warps.plugin.getConfig().getString("Messages.NoPermissions").replaceAll("&", "§"));
        nopermitem.setItemMeta(nopermmeta);
        final ItemStack closeitem = new ItemStack(Material.valueOf("" + Warps.plugin.getConfig().getString("Icons.CloseMenu").replaceAll("&", "§")));
        final ItemMeta closemeta = closeitem.getItemMeta();
        closemeta.setDisplayName("" + Warps.plugin.getConfig().getString("Messages.CloseMenu").replaceAll("&", "§"));
        closeitem.setItemMeta(closemeta);
        if (p.hasPermission("warps.warp")) {
            inv.setItem(10, warpitem);
        } else {
            inv.setItem(10, nopermitem);
        }
        if (p.hasPermission("warps.home")) {
            inv.setItem(12, homeitem);
        } else {
            inv.setItem(12, nopermitem);
        }
        if (Warps.plugin.getConfig().getBoolean("watch")) {
            inv.setItem(14, watchitem);
        } else {
            inv.setItem(14, paperitem);
        }
        if (!p.hasPermission("warps.togglewatch")) {
            inv.setItem(14, nopermitem);
        }
        if (p.hasPermission("warps.spawn")) {
            inv.setItem(16, spawnitem);
        } else {
            inv.setItem(16, nopermitem);
        }
        inv.setItem(22, closeitem);
        p.openInventory(inv);
    }

    public Inventory warpInv(final Player p) {
        final Inventory inv = Bukkit.createInventory((InventoryHolder) null, 54, "" + Warps.plugin.getConfig().getString("Messages.Warps").replaceAll("&", "§"));
        Warps.plugin.reloadConfig();
        final ConfigurationSection warpSection = Warps.plugin.getConfig().getConfigurationSection("warps");
        final StringBuilder WarpsBuilder = new StringBuilder();
        int s = 0;
        for (final String warp2 : warpSection.getKeys(false)) {
            final Object[] name = warpSection.getKeys(false).toArray();
            Inv.warpitem.add(new ItemStack(Material.valueOf(Warps.plugin.getConfig().getString("warps." + name[s] + ".item"))));
            final ItemMeta warpmeta = Inv.warpitem.get(s).getItemMeta();
            warpmeta.setDisplayName(name[s].toString());
            Inv.warpitem.get(s).setItemMeta(warpmeta);
            inv.setItem(s, (ItemStack) Inv.warpitem.get(s));
            WarpsBuilder.append(warp2);
            ++s;
        }
        p.openInventory(inv);
        return inv;
    }

    public Inventory editInv(final Player p) {
        final Inventory inv = Bukkit.createInventory((InventoryHolder) null, 54, "" + Warps.plugin.getConfig().getString("Messages.Icons").replaceAll("&", "§"));
        final ItemStack ROSE = new ItemStack(Material.RED_ROSE);
        final ItemStack RAILS = new ItemStack(Material.RAILS);
        final ItemStack APPLE = new ItemStack(Material.APPLE);
        final ItemStack BEDD = new ItemStack(Material.BED);
        final ItemStack BLAZE_POWDER = new ItemStack(Material.BLAZE_POWDER);
        final ItemStack IRON_DOOR = new ItemStack(Material.IRON_DOOR);
        final ItemStack IRON_BLOCK = new ItemStack(Material.IRON_BLOCK);
        final ItemStack CHEST = new ItemStack(Material.CHEST);
        final ItemStack BUCKET = new ItemStack(Material.BUCKET);
        final ItemStack BEDROCK = new ItemStack(Material.BEDROCK);
        final ItemStack GRASS = new ItemStack(Material.GRASS);
        final ItemStack DIRT = new ItemStack(Material.DIRT);
        final ItemStack LOG = new ItemStack(Material.LOG);
        final ItemStack SPONGE = new ItemStack(Material.SPONGE);
        final ItemStack LEAVES = new ItemStack(Material.LEAVES);
        final ItemStack TNT = new ItemStack(Material.TNT);
        final ItemStack GLOWSTONE = new ItemStack(Material.GLOWSTONE);
        final ItemStack DIABLOCK = new ItemStack(Material.DIAMOND_BLOCK);
        final ItemStack COAL = new ItemStack(Material.COAL);
        final ItemStack LAPIS = new ItemStack(Material.INK_SACK, 1, (short) 4);
        final ItemStack REDSTONE = new ItemStack(Material.REDSTONE);
        final ItemStack GOLD = new ItemStack(Material.GOLD_INGOT);
        final ItemStack IRON = new ItemStack(Material.IRON_INGOT);
        final ItemStack QUARTZ = new ItemStack(Material.QUARTZ);
        final ItemStack DIAMOND = new ItemStack(Material.DIAMOND);
        final ItemStack EMERALD = new ItemStack(Material.EMERALD);
        final ItemStack PICKAXE = new ItemStack(Material.DIAMOND_PICKAXE);
        final ItemStack SEEDS = new ItemStack(Material.SEEDS);
        final ItemStack MELON = new ItemStack(Material.MELON);
        final ItemStack PUMPKIN = new ItemStack(Material.PUMPKIN);
        final ItemStack SAPLING = new ItemStack(Material.SAPLING);
        final ItemStack GRASSTALL = new ItemStack(Material.LONG_GRASS);
        final ItemStack MUSHROOM = new ItemStack(Material.BROWN_MUSHROOM);
        final ItemStack MUSHROOMRED = new ItemStack(Material.RED_MUSHROOM);
        final ItemStack VINE = new ItemStack(Material.VINE);
        final ItemStack LILY = new ItemStack(Material.WATER_LILY);
        final ItemStack SAND = new ItemStack(Material.SAND);
        final ItemStack WOOD = new ItemStack(Material.WOOD);
        final ItemStack MYCEL = new ItemStack(Material.MYCEL);
        final ItemStack BREWING = new ItemStack(Material.BREWING_STAND_ITEM);
        final ItemStack CHESTENDER = new ItemStack(Material.ENDER_CHEST);
        final ItemStack CRAFTING = new ItemStack(Material.WORKBENCH);
        final ItemStack FURNACE = new ItemStack(Material.FURNACE);
        final ItemStack ANVIL = new ItemStack(Material.ANVIL);
        final ItemStack ENCHANT = new ItemStack(Material.ENCHANTMENT_TABLE);
        final ItemStack CARROT = new ItemStack(Material.CARROT_ITEM);
        final ItemStack POTATO = new ItemStack(Material.POTATO_ITEM);
        final ItemStack STEAK = new ItemStack(Material.COOKED_BEEF);
        final ItemStack PORKCHOP = new ItemStack(Material.GRILLED_PORK);
        final ItemStack CHICKEN = new ItemStack(Material.COOKED_CHICKEN);
        final ItemStack MUTTON = new ItemStack(Material.COOKED_MUTTON);
        final ItemStack PIE = new ItemStack(Material.PUMPKIN_PIE);
        final ItemStack FISH = new ItemStack(Material.COOKED_FISH);
        final ItemStack GOLDAPPLE = new ItemStack(Material.GOLDEN_APPLE);
        inv.setItem(0, ROSE);
        inv.setItem(1, RAILS);
        inv.setItem(2, APPLE);
        inv.setItem(3, BEDD);
        inv.setItem(4, BLAZE_POWDER);
        inv.setItem(5, IRON_DOOR);
        inv.setItem(6, IRON_BLOCK);
        inv.setItem(7, CHEST);
        inv.setItem(8, BUCKET);
        inv.setItem(9, BEDROCK);
        inv.setItem(10, GRASS);
        inv.setItem(11, DIRT);
        inv.setItem(12, LOG);
        inv.setItem(13, SPONGE);
        inv.setItem(14, LEAVES);
        inv.setItem(15, TNT);
        inv.setItem(16, GLOWSTONE);
        inv.setItem(17, DIABLOCK);
        inv.setItem(18, COAL);
        inv.setItem(19, LAPIS);
        inv.setItem(20, REDSTONE);
        inv.setItem(21, GOLD);
        inv.setItem(22, IRON);
        inv.setItem(23, QUARTZ);
        inv.setItem(24, DIAMOND);
        inv.setItem(25, EMERALD);
        inv.setItem(26, PICKAXE);
        inv.setItem(27, SEEDS);
        inv.setItem(28, MELON);
        inv.setItem(29, PUMPKIN);
        inv.setItem(30, SAPLING);
        inv.setItem(31, GRASSTALL);
        inv.setItem(32, MUSHROOM);
        inv.setItem(33, MUSHROOMRED);
        inv.setItem(34, VINE);
        inv.setItem(35, LILY);
        inv.setItem(36, SAND);
        inv.setItem(37, WOOD);
        inv.setItem(38, MYCEL);
        inv.setItem(39, BREWING);
        inv.setItem(40, CHESTENDER);
        inv.setItem(41, CRAFTING);
        inv.setItem(42, FURNACE);
        inv.setItem(43, ANVIL);
        inv.setItem(44, ENCHANT);
        inv.setItem(45, CARROT);
        inv.setItem(46, POTATO);
        inv.setItem(47, STEAK);
        inv.setItem(48, PORKCHOP);
        inv.setItem(49, CHICKEN);
        inv.setItem(50, MUTTON);
        inv.setItem(51, PIE);
        inv.setItem(52, FISH);
        inv.setItem(53, GOLDAPPLE);
        p.openInventory(inv);
        return inv;
    }

    public Inventory homeInv(final Player p) {
        final Inventory inv = Bukkit.createInventory((InventoryHolder) null, 27, "" + Warps.plugin.getConfig().getString("Messages.Homes").replaceAll("&", "§"));
        final ConfigurationSection homeSection = Warps.plugin.getConfig().getConfigurationSection("homes." + p.getUniqueId());
        final StringBuilder HomeBuilder = new StringBuilder();
        int s = 0;
        for (final String warp2 : homeSection.getKeys(false)) {
            final Object[] name = homeSection.getKeys(false).toArray();
            final ItemStack warpitem = new ItemStack(Material.valueOf("" + Warps.plugin.getConfig().getString("Icons.HomeIcon")));
            final ItemMeta warpmeta = warpitem.getItemMeta();
            warpmeta.setDisplayName(name[s].toString());
            final List<String> lores = new ArrayList<String>();
            lores.add("§7Klicke um dich zu teleportieren.");
            warpmeta.setLore((List) lores);
            warpitem.setItemMeta(warpmeta);
            inv.setItem(s, warpitem);
            HomeBuilder.append(warp2);
            ++s;
        }
        p.openInventory(inv);
        return inv;
    }

    static {
        Inv.warpitem = new ArrayList<ItemStack>();
    }
}
