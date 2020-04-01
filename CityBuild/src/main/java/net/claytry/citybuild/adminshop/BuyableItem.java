package net.claytry.citybuild.adminshop;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Material;
@AllArgsConstructor
@Data
public final class BuyableItem {

    private Material material;
    private int subId;
    private double price;
}
