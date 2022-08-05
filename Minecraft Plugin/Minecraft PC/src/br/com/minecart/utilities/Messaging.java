package br.com.minecart.utilities;

import org.bukkit.ChatColor;

import br.com.minecart.Minecart;

public class Messaging
{
    public static String format(String message, Boolean usePrefix, Boolean getConfigMessage)
    {
        String newMessage = "";

        if (message == null || message.isEmpty()) {
            return "";
        };

        if (usePrefix) {
            newMessage += Minecart.instance.getConfig().getString("config.prefix", "&b[Minecart] ");
        };

        if (getConfigMessage) {
            if (Minecart.instance.ResourceMessage.getString(message) != null) {
                newMessage += Minecart.instance.ResourceMessage.getString(message);
            } else {
                newMessage = "&b[Minecart] &c" + message + " Not Found.";
            }
        } else {
            newMessage += message;
        }

        return ChatColor.translateAlternateColorCodes('&', newMessage);
    }
}