package br.com.minecart.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.com.minecart.Minecart;
import br.com.minecart.storage.LOGStorage;
import br.com.minecart.utilities.HttpRequest;
import br.com.minecart.utilities.HttpResponse;
import br.com.minecart.utilities.Messaging;

public class RedeemCash implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player player = (Player) sender;
		
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("username", player.getName());
		
		HttpResponse response = HttpRequest.httpRequest(Minecart.instance.MINECART_API + "/shop/player/redeemcash", params);
		
		if(response == null){
			player.sendMessage(Messaging.format("error.internal-error", false, true));
			
			return false;
		}
		
		try {
			JsonObject jsonObject = new JsonParser().parse(response.response).getAsJsonObject();
			
			if(response.responseCode == 200){
				int quantity = jsonObject.get("cash").getAsInt();
				
				if(quantity > 0){
					this.deliverCash(player, quantity);
				}
			} else {
				this.processHttpError(player, response);
			}
		} catch(Exception e){
			player.sendMessage(Messaging.format("error.internal-error", false, true));
		}
		
		return false;
	}
	
	private void deliverCash(Player player, int quantity){
		String cmdTemp = Minecart.instance.getConfig().getString("cmd.cmd_active_cash");
		
		cmdTemp = cmdTemp.replace("{player.name}", player.getName());
		cmdTemp = cmdTemp.replace("{cash.quantity}", String.valueOf(quantity));
		
		if(Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmdTemp)){
			String msg = Minecart.instance.ResourceMessage.getString("success.redeem-cash");
			
			msg = msg.replace("{player.name}", player.getName());
			
			player.sendMessage(Messaging.format(msg, true, false));
		} else {
			String msg = Minecart.instance.ResourceMessage.getString("error.redeem-cash");
			
			msg = msg.replace("{cash.amount}", String.valueOf(quantity));
			
			player.sendMessage(Messaging.format("error.internal-error", true, true));
			player.sendMessage(Messaging.format(msg, true, false));
			
			LOGStorage.resgatarCASH("[ERROR] Ocorreu um erro ao dar ( "+ String.valueOf(quantity) +" ) de CASH para o jogador ( " + player.getName() + " ).");
		}
	}
	
	private void processHttpError(Player player, HttpResponse response){
		if(player.hasPermission("minecart.admin")){
	    	if(response.responseCode == 401){
	    		player.sendMessage(Messaging.format("error.invalid-shopkey", false, true));
	    	} else {
	    		try {
	    			JsonParser JsonParser = new JsonParser();
	    			
	    			JsonObject jsonObject = JsonParser.parse(response.response).getAsJsonObject();
	    			
	    			Integer error_code = jsonObject.get("code").getAsInt();
	    			
	    			if(error_code == 40010){
	    				player.sendMessage(Messaging.format("error.nothing-products-cash", true, true));
	    			} else if(error_code == 40011){
	    				player.sendMessage(Messaging.format("error.invalid-shopserver", false, true));
	    			} else {
	    				player.sendMessage(Messaging.format("error.internal-error", false, true));
	    			}
	    		} catch(Exception e){
	    			player.sendMessage(Messaging.format("error.internal-error", false, true));
	    		}
	    	}
		} else {
			player.sendMessage(Messaging.format("error.internal-error", false, true));
		}
	}
}
