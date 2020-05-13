package br.com.minecart.commands;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.minecart.MineCart;
import br.com.minecart.MineCartKey;
import br.com.minecart.utilities.Messaging;

public class MinhasKeysCommand implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player player = (Player) sender;
		
		ArrayList<String> minecartKeys = MineCart.instance.keysManage.getPlayerKeys(player);
		
		if(minecartKeys != null){
			player.sendMessage(Messaging.format("success.player-list-keys-title", true));
			player.sendMessage("");
			
			for(String key : minecartKeys){
				MineCartKey minecartKey = MineCart.instance.keysManage.getKey(key);
				
				String msg = MineCart.instance.ResourceMessage.getString("success.player-list-keys-key");
				
				msg = msg.replace("{key.group}", minecartKey.getGrup());
				msg = msg.replace("{key.duration}", String.valueOf(minecartKey.getDuration()));
				
				player.sendMessage(msg);
			}
		} else {
			player.sendMessage(Messaging.format("error.inform-key", true));
		}
		
		return false;
	}
}
