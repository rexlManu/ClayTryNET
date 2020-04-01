package net.claytry.citybuild.adminshop;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Data
@AllArgsConstructor
public final class AdminShop {

    private Material displayItem;
    private String categoryName;
    private List<BuyableItem> buyableItems;
}
