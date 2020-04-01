package net.claytry.citybuild.adminshop;

import net.claytry.citybuild.CityBuild;
import net.claytry.citybuild.DATA;
import net.claytry.citybuild.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class AdminShopManager implements Listener {

    private File file;
    private YamlConfiguration configuration;
    private CityBuild cityBuild;
    private List<AdminShop> adminShops;

    public AdminShopManager(CityBuild cityBuild) {
        this.cityBuild = cityBuild;
        Bukkit.getPluginManager().registerEvents(this, cityBuild);
        this.file = new File(cityBuild.getDataFolder(), "shops.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.configuration = YamlConfiguration.loadConfiguration(file);
        this.adminShops = new ArrayList<>();


        if (!configuration.contains("categories")) {
            configuration.set("categories", new String[]{"Default"});
            save();
        }

        for (String categories : configuration.getStringList("categories")) {
            if (!configuration.contains(categories + ".Items")) {
                configuration.set(categories + ".Items", new String[]{"DIAMOND:500"});
                configuration.set(categories + ".Display", Material.BEDROCK.name());
                save();
                adminShops.add(new AdminShop(Material.BEDROCK, categories, Arrays.asList(new BuyableItem(Material.DIAMOND, 0, 500))));
            } else {
                List<BuyableItem> buyableItems = new ArrayList<>();
                for (String s : configuration.getStringList(categories + ".Items")) {
                    String[] split = s.split(":");

                    int subID = 0;
                    Material material = null;

                    if (split[0].contains("#")) {
                        String[] strings = split[0].split("#");
                        material = Material.valueOf(strings[0].toUpperCase());
                        subID = Integer.parseInt(strings[1]);
                    } else {
                        material = Material.valueOf(split[0].toUpperCase());
                    }

                    Double price = Double.valueOf(split[1]);
                    buyableItems.add(new BuyableItem(material, subID, price));
                }
                if (!configuration.contains(categories + ".Display")) {
                    configuration.set(categories + ".Display", Material.BEDROCK.name());
                    save();
                }
                adminShops.add(new AdminShop(Material.valueOf(configuration.getString(categories + ".Display")), categories, buyableItems));
            }
        }
    }

    public void openMainInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9, "§8» §2AdminShop");
        for (AdminShop adminShop : adminShops) {
            ItemStack itemStack = new ItemStack(adminShop.getDisplayItem(), 1);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("§8» §2" + adminShop.getCategoryName());
            itemStack.setItemMeta(itemMeta);
            inventory.addItem(itemStack);
        }
        player.openInventory(inventory);
    }

    public void openCategory(Player player, AdminShop adminShop) {
        Inventory inventory = Bukkit.createInventory(null, 6 * 9, "§8» §2" + adminShop.getCategoryName());

        for (BuyableItem buyableItem : adminShop.getBuyableItems()) {
            ItemStack itemStack = new ItemStack(buyableItem.getMaterial(), 1, (short) buyableItem.getSubId());
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("§8» §2" + shortName(buyableItem.getMaterial().name()));
            itemMeta.setLore(Arrays.asList("§r", "§8» §71§8x §7Kaufen mit Linksklick für §a" + buyableItem.getPrice(),
                    "§8» §764§8x §7Kaufen mit Shift+Linksklick für §a" + (64 * buyableItem.getPrice()),
                    "§8» §71§8x §7Verkaufen mit Rechtsklick für §a" + Utils.formatDouble((buyableItem.getPrice() / DATA.GETEILT)),
                    "§8» §764§8x §7Verkaufen mit Shift+Rechtsklick für §a" + Utils.formatDouble(64 * (buyableItem.getPrice() / DATA.GETEILT)), "§r"));
            // itemMeta.setLore(Arrays.asList("§8» §764§8x §7Kaufen mit Shift+Rechtsklick für §a" + buyableItem.getPrice()));
            // itemMeta.setLore(Arrays.asList("§8» §71§8x §7Verkaufen mit Linksklick für §a" + Utils.formatDouble((buyableItem.getPrice() / 2))));
            // itemMeta.setLore(Arrays.asList("§8» §764§8x §7Verkaufen mit Shift+Linksklick für §a" + Utils.formatDouble((buyableItem.getPrice() / 2))));
            itemStack.setItemMeta(itemMeta);
            inventory.addItem(itemStack);
        }

        player.openInventory(inventory);

    }

    @EventHandler
    public void on(InventoryCloseEvent event) {

    }

    @EventHandler
    public void on(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            if (event.getClickedInventory() == null) return;
            String title = event.getClickedInventory().getTitle();
            if (title.equalsIgnoreCase("§8» §2AdminShop")) {
                event.setCancelled(true);
                if (event.getCurrentItem() == null) return;
                if (event.getCurrentItem().getItemMeta() == null) return;
                if (!event.getCurrentItem().getItemMeta().hasDisplayName()) return;
                for (AdminShop adminShop : adminShops) {
                    if (event.getCurrentItem().getItemMeta().getDisplayName().contains(adminShop.getCategoryName())) {


                        openCategory(player, adminShop);
                        return;
                    }
                }
            }

            if (title.startsWith("§8» §2")) {
                for (AdminShop adminShop : adminShops) {
                    if (title.contains(adminShop.getCategoryName())) {
                        event.setCancelled(true);

                        if (event.getCurrentItem() == null) return;
                        if (event.getCurrentItem().getItemMeta() == null) return;
                        if (!event.getCurrentItem().getItemMeta().hasDisplayName()) return;

                        for (BuyableItem buyableItem : adminShop.getBuyableItems()) {
                            if (buyableItem.getMaterial() == event.getCurrentItem().getType() && buyableItem.getSubId() == event.getCurrentItem().getDurability()) {

                                boolean first = true;

                                boolean leftClick = event.isLeftClick();
                                boolean rightClick = event.isRightClick();
                                boolean shiftClick = event.isShiftClick();

                                int amount = 1;
                                if (shiftClick) {
                                    amount = 64;
                                }


                                for (int i = 0; i < amount; i++) {
                                    PlayerInventory inventory1 = player.getInventory();
                                    if (leftClick) {
                                        if (cityBuild.getMoneyManager().hasEnoughMoney(player.getUniqueId(), buyableItem.getPrice())) {
                                            if (inventory1.firstEmpty() == -1) {
                                                player.sendMessage(cityBuild.getPrefix() + "§7Dein Inventar ist voll.");
                                                break;
                                            }
                                            cityBuild.getMoneyManager().removeMoney(player.getUniqueId(), buyableItem.getPrice());
                                            inventory1.addItem(new ItemStack(buyableItem.getMaterial(), 1, (short) buyableItem.getSubId()));
                                            if (first) {
                                                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 1f);
                                                first = false;
                                            }

                                        } else {
                                            player.sendMessage(cityBuild.getPrefix() + "§7Du hast nicht genug Geld.");
                                            break;
                                        }
                                    } else {

                                        PlayerInventory inventory = inventory1;

                                        if (inventory.containsAtLeast(new ItemStack(buyableItem.getMaterial(), 1, (short) buyableItem.getSubId()), 1)) {
                                            cityBuild.getMoneyManager().addMoney(player.getUniqueId(), Double.valueOf(Utils.formatDouble(buyableItem.getPrice() / DATA.GETEILT)));
                                            removeItems(inventory, buyableItem.getMaterial(), 1);
                                            if (first) {
                                                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 1f);
                                                first = false;
                                            }

                                        } else {
                                            player.sendMessage(cityBuild.getPrefix() + "§7Du hast nicht genug Items.");
                                            break;
                                        }
                                    }
                                }


                                return;
                            }
                        }
                        return;
                    }
                }
            }

            InventoryView openInventory = player.getOpenInventory();
            if (openInventory.getTitle().equalsIgnoreCase("§8» §2AdminShop")) {
                event.setCancelled(true);
            } else if (openInventory.getTitle().startsWith("§8» §2")) {
                event.setCancelled(true);
            }
        }
    }

    private String shortName(String name) {
        String[] data = name.split("_");
        StringBuilder stringBuilder = new StringBuilder();
        for (String datum : data) {
            stringBuilder.append(upperCase(datum)).append(" ");
        }
        return stringBuilder.toString();
    }

    private String upperCase(String name) {
        String[] split = name.split("");
        split[0] = split[0].toUpperCase();
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : split) {
            stringBuilder.append(s);
        }
        return stringBuilder.toString();
    }

    private void load() {
        try {
            configuration.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void save() {
        try {
            this.configuration.save(file);
        } catch (IOException e) {


        }
    }

    public static void removeItems(final Inventory inventory, final Material type, int amount) {
        if (amount <= 0) {
            return;
        }
        final int size = inventory.getSize();
        for (int slot = 0; slot < size; slot++) {
            final ItemStack is = inventory.getItem(slot);
            if (is == null) {
                continue;
            }
            if (type == is.getType()) {
                final int newAmount = is.getAmount() - amount;
                if (newAmount > 0) {
                    is.setAmount(newAmount);
                    break;
                } else {
                    inventory.clear(slot);
                    amount = -newAmount;
                    if (amount == 0) {
                        break;
                    }
                }
            }
        }
    }
}
