package br.com.minecart.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.com.minecart.MineCart;
import br.com.minecart.storage.LOGStorage;
import br.com.minecart.utilities.HttpRequest;
import br.com.minecart.utilities.Messaging;

public class ResgatarVipCommand implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player player = (Player) sender;
		
		try {
			if(!MineCart.getDB().checkConnection()) throw new Exception("Error connection database!");
		} catch(Exception e){
			player.sendMessage(Messaging.format("error.internal-error", true));
			
			return true;
		}
		
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("username", player.getName());
		
		Boolean getAnyoneProdct = false;
		
		String products = HttpRequest.UrlJsonRequest(MineCart.instance.MineCartAPI + "/getProducts", params);
		
		if(products == null || products.isEmpty()){
			player.sendMessage(Messaging.format("error.nothing-products", true));
			
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
					
					for(int i = 1; i <= quantity; i++){
						if(MineCart.instance.keysManage.newKey(player.getName(), group, duration)){
							if(!getAnyoneProdct) getAnyoneProdct = true;
						} else {
							String msg = MineCart.instance.ResourceMessage.getString("error.redeem-vip");
							
							msg = msg.replace("{key.group}", group);
							msg = msg.replace("{key.duration}", String.valueOf(quantity));
							
							player.sendMessage(Messaging.format(msg, false));
							
							LOGStorage.resgatarVIP("[ERROR] Ocorreu um erro ao dar o VIP ( "+ group +" ) com duração de ( " + duration + " ) DIAS.");
						}
					}
				}
				
				if(getAnyoneProdct){
					String msg = MineCart.instance.ResourceMessage.getString("success.redeem-vip");
					
					msg = msg.replace("{player.name}", player.getName());
					
					player.sendMessage(Messaging.format(msg, false));
					
					return true;
				}
			}
		} catch(Exception e){
			
		}
		
		player.sendMessage(Messaging.format("error.nothing-products", true));
		
		return false;
	}
}
