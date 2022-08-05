package br.com.minecart.utilities;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Utils
{
    public static Boolean playerInventoryClean(Player player)
    {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) {
                return false;
            }
        }

        return true;
    }
}