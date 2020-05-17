package br.com.minecart.utilities;

import org.bukkit.ChatColor;

import br.com.minecart.MineCart;

public class Messaging {
	public static String format(String message, Boolean usePrefix, Boolean getConfigMessage){
		String newMessage = "";
		
		if(message == null || message.isEmpty()) return "";
		 
		if(usePrefix) newMessage += MineCart.instance.getConfig().getString("prefix", "&b[MineCart] ");
		
		 if(getConfigMessage){
			 if(MineCart.instance.ResourceMessage.getString(message) != null){
				 newMessage += MineCart.instance.ResourceMessage.getString(message);
			 } else {
				 newMessage = "§b[MineCart] §c" + message + " Not Found.";
			 }
		 } else {
			 newMessage += message;
		 }
		 
		 return ChatColor.translateAlternateColorCodes('&', newMessage);
	}
}
