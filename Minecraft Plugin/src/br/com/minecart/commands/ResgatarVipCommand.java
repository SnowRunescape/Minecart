package br.com.minecart.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.com.minecart.MineCart;
import br.com.minecart.utilities.HttpRequest;
import br.com.minecart.utilities.Messaging;

public class ResgatarVipCommand implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("username", sender.getName());
		
		String products = HttpRequest.UrlJsonRequest(MineCart.instance.MineCartAPI + "/getProducts", params);
		
		if(products == null || products.isEmpty()){
			sender.sendMessage(Messaging.format("error.nothing-products", true));
			
			return true;
		}
		
		try {
			JsonObject jsonObject = new JsonParser().parse(products).getAsJsonObject();
			
			JsonElement StatusAPI = jsonObject.get("status");
			
			if(StatusAPI.getAsInt() == 200){
				JsonArray productsPlayer = jsonObject.getAsJsonArray("products");
				
				for (JsonElement product : productsPlayer){
					JsonObject productObj = product.getAsJsonObject();
					
					String group = productObj.get("item").getAsString();
					Integer quantity = productObj.get("quantity").getAsInt();
					Integer duration = productObj.get("duration").getAsInt();
					
					for(int i = 1; i <= quantity; i++) MineCart.instance.keysManage.newKey(sender.getName(), group, duration);
				}
				
				return true;
			}
		} catch(Exception e){
			
		}
		
		sender.sendMessage(Messaging.format("error.nothing-products", true));
		
		return false;
	}

}
