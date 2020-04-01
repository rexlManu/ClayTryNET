package net.claytry.citybuild.command;

import net.claytry.citybuild.CityBuild;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class MoneyCommand implements CommandExecutor {

    private CityBuild cityBuild;

    public MoneyCommand(CityBuild cityBuild) {
        this.cityBuild = cityBuild;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if(player.hasPermission("claytry.admin")){
            if(args.length == 1){
                try {
                    cityBuild.getMoneyManager().addMoney(player.getUniqueId(), Double.parseDouble(args[0]));
                }catch (Exception e){
                    player.sendMessage("error");
                }
                return true;
            }
        }
        double money = cityBuild.getMoneyManager().getMoney(player.getUniqueId());
        player.sendMessage(cityBuild.getPrefix() + "§7Auf deinem Konto sind §a" + money + "§7€.");
        return false;
    }
}
