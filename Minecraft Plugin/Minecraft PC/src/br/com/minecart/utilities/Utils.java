package br.com.minecart.utilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

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

    public static String[] convertJsonArrayToStringArray(JsonArray jsonArray)
    {
        List<String> arrayList = new ArrayList<String>();

        for (JsonElement teste : jsonArray) {
            arrayList.add(teste.getAsString());
        }

        return arrayList.toArray(new String[arrayList.size()]);
    }
}