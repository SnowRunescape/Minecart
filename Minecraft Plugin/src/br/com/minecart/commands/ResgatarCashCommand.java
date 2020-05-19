package br.com.minecart.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.com.minecart.MineCart;
import br.com.minecart.utilities.HttpRequest;
import br.com.minecart.utilities.Messaging;

public class ResgatarCashCommand implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player player = (Player) sender;
		
		try {
			if(!MineCart.getDB().checkConnection()) throw new Exception("Error connection database!");
		} catch(Exception e){
			player.sendMessage(Messaging.format("error.internal-error", true, true));
			
			return true;
		}
		
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("type", "MineCart.CASH");
		params.put("username", player.getName());
		
		Boolean getAnyoneProdct = false;
		
		String products = HttpRequest.UrlJsonRequest(MineCart.instance.MineCartAPI + "/getProducts", params);
		
		if(products == null || products.isEmpty()){
			player.sendMessage(Messaging.format("error.nothing-products-cash", true, true));
			
			return true;
		}
		
		try {
			JsonObject jsonObject = new JsonParser().parse(products).getAsJsonObject();
			
			JsonElement StatusAPI = jsonObject.get("status");
			
			if(StatusAPI.getAsInt() == 200){
				int cashAmount = jsonObject.get("cash").getAsInt();
				
				if(cashAmount > 0){
					if(!getAnyoneProdct) getAnyoneProdct = true;
					
					String cmdTemp = MineCart.instance.getConfig().getString("cmd.cmd_active_cash");
					
					cmdTemp = cmdTemp.replace("{player.name}", player.getName());
					cmdTemp = cmdTemp.replace("{cash.quantity}", String.valueOf(cashAmount));
					
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmdTemp);
				}
				
				if(getAnyoneProdct){
					String msg = MineCart.instance.ResourceMessage.getString("success.redeem-cash");
					
					msg = msg.replace("{player.name}", player.getName());
					
					player.sendMessage(Messaging.format(msg, true, false));
					
					return true;
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		
		player.sendMessage(Messaging.format("error.nothing-products-cash", true, true));
		
		return false;
	}
}
