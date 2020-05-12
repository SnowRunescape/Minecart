package br.com.minecart.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.minecart.MineCart;

public class AtivarCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length == 0){
			//informe a key para ativar
			return false;
		}
		
		if(MineCart.instance.keysManage.getKey(args[0]) != null){
			MineCart.instance.keysManage.useKey((Player) sender, args[0]);
			
			return true;
		}
		
		return false;
	}

}
