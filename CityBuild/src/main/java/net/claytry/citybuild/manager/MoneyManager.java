package net.claytry.citybuild.manager;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoDatabase;
import com.arangodb.entity.BaseDocument;
import net.claytry.citybuild.CityBuild;
import org.bukkit.Bukkit;

import java.util.UUID;

public final class MoneyManager {

    private CityBuild cityBuild;
    private ArangoCollection collection;

    public MoneyManager(CityBuild cityBuild) {
        this.cityBuild = cityBuild;
        collection = cityBuild.getArangoConnector().getDatabase().collection("players");
    }

    public double getMoney(UUID uuid) {
        BaseDocument document = collection.getDocument(uuid.toString(), BaseDocument.class);
        if (!document.getProperties().containsKey("money")) {
            document.addAttribute("money", 0);
            collection.updateDocument(uuid.toString(), document);
        }
        return Double.parseDouble(document.getAttribute("money").toString());
    }

    public void setMoney(UUID uuid, double money) {
        BaseDocument document = collection.getDocument(uuid.toString(), BaseDocument.class);
        document.addAttribute("money", money);
        collection.updateDocument(uuid.toString(), document);

        if (Bukkit.getPlayer(uuid) != null) {
            cityBuild.getPlayerScoreboard().updateMoney(Bukkit.getPlayer(uuid));
        }
    }

    public void addMoney(UUID uuid, double money) {
        setMoney(uuid, getMoney(uuid) + money);
    }

    public void removeMoney(UUID uuid, double money) {
        setMoney(uuid, getMoney(uuid) - money);
    }

    public void transferMoney(UUID receiver, UUID sender, double money) {
        removeMoney(sender, money);
        addMoney(receiver, money);
    }

    public boolean hasEnoughMoney(UUID uuid, double money) {
        return getMoney(uuid) >= money;
    }
}
