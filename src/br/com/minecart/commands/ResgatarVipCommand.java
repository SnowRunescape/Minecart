package br.com.minecart.commands;

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

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		String products = HttpRequest.UrlJsonRequest(MineCart.instance.MineCartAPI + "/getProducts?&username=" + sender.getName());
		
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
					
					Integer id = productObj.get("id").getAsInt();
					String item = productObj.get("item").getAsString();
					Integer quantity = productObj.get("quantity").getAsInt();
					Integer duration = productObj.get("duration").getAsInt();
				}
				
				return true;
			}
		} catch(Exception e){
			
		}
		
		sender.sendMessage(Messaging.format("error.nothing-products", true));
		
		return false;
	}

}
