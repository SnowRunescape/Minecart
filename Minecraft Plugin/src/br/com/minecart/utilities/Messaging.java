package br.com.minecart.utilities;

import org.bukkit.ChatColor;

import br.com.minecart.MineCart;

public class Messaging {
	public static String format(String message, Boolean getConfigMessage){
		 if(message == null || message.isEmpty()) return "";
		 
		 if(getConfigMessage){
			 if(MineCart.instance.ResourceMessage.getString(message) != null){
				 message = MineCart.instance.ResourceMessage.getString(message);
			 } else {
				 message = "§b[MineCart] §c" + message + " Not Found.";
			 }
		 }
		 
		 return ChatColor.translateAlternateColorCodes('&', message);
	}
}
