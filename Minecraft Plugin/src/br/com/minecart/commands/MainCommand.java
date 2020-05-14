package br.com.minecart.commands;

import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.Maps;

import br.com.minecart.utilities.Messaging;

public class MainCommand implements CommandExecutor {
	private Map<String, CommandExecutor> CommandMap = Maps.newHashMap();
	
	public MainCommand(){
		CommandMap.put("minecart", new MineCartCommand());
		CommandMap.put("minhaskeys", new MinhasKeysCommand());
		CommandMap.put("ativar", new AtivarCommand());
		CommandMap.put("resgatarvip", new ResgatarVipCommand());
		CommandMap.put("resgatarcash", new ResgatarCashCommand());
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(!(sender instanceof Player)){
            sender.sendMessage(Messaging.format("error.player-only", true));
            
            return true;
        }
		
		Player player = (Player) sender;
		commandLabel = commandLabel.toLowerCase();
		
		 if(CommandMap.containsKey(commandLabel)){
			 CommandExecutor command = CommandMap.get(commandLabel);
			 
		        if(!hasPermission(player, command)){
		            player.sendMessage(Messaging.format("error.insufficient-permissions", true));
		            
		            return true;
		        }

		        return command.onCommand(sender, cmd, commandLabel, args);
		 }
		
		return false;
	}
	
	private boolean hasPermission(Player bukkitPlayer, CommandExecutor cmd) {
		CommandPermissions permissions = cmd.getClass().getAnnotation(CommandPermissions.class);
		
        if(permissions == null) return true;

        for(String permission : permissions.value()){
            if(bukkitPlayer.hasPermission(permission)){
            	return true;
            }
        }

        return false;
    }
}
