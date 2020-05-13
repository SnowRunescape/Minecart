package br.com.minecart.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.minecart.MineCart;
import br.com.minecart.utilities.Messaging;

public class AtivarCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length == 0){
			sender.sendMessage(Messaging.format("error.inform-key", true));
			
			return false;
		}
		
		if(MineCart.instance.keysManage.getKey(args[0]) != null){
			if(MineCart.instance.keysManage.useKey((Player) sender, args[0])){
				//key ativada com sucesso
				
				return true;
			} else {
				//aconteceu um erro ao ativar a key, tente novamente
				sender.sendMessage(Messaging.format("error.active-key", true));
			}
		} else {
			//kikar
			//a key informada nao existe
			//error.does-not-exist-key
		}
		
		return false;
	}
}
