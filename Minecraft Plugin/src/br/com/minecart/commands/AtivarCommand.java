package br.com.minecart.commands;

import java.util.Iterator;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import br.com.minecart.MineCart;
import br.com.minecart.MineCartKey;
import br.com.minecart.utilities.Messaging;

public class AtivarCommand implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player player = (Player) sender;
		
		if(args.length == 0){
			player.sendMessage(Messaging.format("error.inform-key", true, true));
			
			return false;
		}
		
		for (ItemStack item : player.getInventory().getContents()){
            if(item != null) {
            	player.sendMessage(Messaging.format("error.clean-inventory", true, true));
            	
                return false;
            }
        }
		
		MineCartKey minecartKey = MineCart.instance.keysManage.getKey(args[0]);
		
		if(minecartKey != null){
			if(MineCart.instance.keysManage.useKey(player, args[0])){
				Iterator<String> msg = MineCart.instance.ResourceMessage.getStringList("success.active-key").iterator();
				
				while(msg.hasNext()){
					String msgTemp = (String) msg.next();
					
					msgTemp = msgTemp.replace("{key.group}", minecartKey.getGrup());
					msgTemp = msgTemp.replace("{key.duration}", String.valueOf(minecartKey.getDuration()));
					msgTemp = msgTemp.replace("{player.name}", player.getName());
					
					player.sendMessage(Messaging.format(msgTemp, false, false));
				}
				
				return true;
			} else {
				sender.sendMessage(Messaging.format("error.active-key", true, true));
			}
		} else {
			String kickMessage = Messaging.format("error.invalid-key", true, true);
			kickMessage = kickMessage.replace("\\n", "\n");
			
			player.kickPlayer(kickMessage);
		}
		
		return false;
	}
}
