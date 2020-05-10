package br.com.minecart.commands;

import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.Maps;

import br.com.minecart.utilities.Messaging;

public class MainCommand implements CommandExecutor {
	private Map<String, CommandExecutor> subCommandMap = Maps.newHashMap();
	
	public MainCommand(){
		subCommandMap.put("reload", new ResgatarVipCommand());
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(!(sender instanceof Player)){
            sender.sendMessage(Messaging.format("error.player-only", true));
            
            return true;
        }
		
		Player player = (Player) sender;
		
		String subCommandName = args[0].toLowerCase();
		
		 if(subCommandMap.containsKey(subCommandName)){
			 CommandExecutor subCommand = subCommandMap.get(subCommandName);
			 
		        if(!hasPermission(player, subCommand)){
		            player.sendMessage(Messaging.format("error.insufficient-permissions", true));
		            return true;
		        }

		        return subCommand.onCommand(sender, cmd, commandLabel, args);
		 }
		
		return false;
	}
	
	private boolean hasPermission(Player bukkitPlayer, CommandExecutor cmd) {
		CommandPermissions permissions = cmd.getClass().getAnnotation(CommandPermissions.class);
		
        if(permissions == null) return true;

        for(String permission : permissions.value()){
            if(bukkitPlayer.hasPermission(permission)) return true;
        }

        return false;
    }
}
