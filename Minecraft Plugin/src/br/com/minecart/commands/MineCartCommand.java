package br.com.minecart.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.minecart.MineCart;

@CommandPermissions("minecart.command.minecart")
public class MineCartCommand implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player player = (Player) sender;
		
		if(args.length == 0){
			player.sendMessage("§b[MineCart] §fLista de comandos:");
			player.sendMessage("§a/MineCart GerarVIP §6- Gerar um §6§lVIP §6para um jogador");
			player.sendMessage("§a/MineCart Reload §6- Recarregar as configurações e o banco de dados");
			
			return true;
		}
		
		if(args[0].equalsIgnoreCase("GerarVIP")){
			if(args.length == 4){
				String playerName = args[1];
				String groupVIP = args[2];
				Integer durationVIP = Integer.valueOf(args[3]);
				
				if(!playerName.matches("[a-zA-Z0-9_]+")){
					player.sendMessage("§b[MineCart] §cO player informado é invalido");
					return true;
				}
				
				if(durationVIP <= 0){
					player.sendMessage("§b[MineCart] §cO tempo informado é invalido");
					return true;
				}
				
				if(MineCart.instance.keysManage.newKey(playerName, groupVIP, durationVIP)){
					player.sendMessage(String.format("§b[MineCart] §cVIP %s de %s DIAS foi gerado com sucesso para o jogador §6%s§c.", groupVIP, durationVIP, playerName));
				} else {
					player.sendMessage("§b[MineCart] §cAconteceu um erro ao gerar o §6§lVIP§c, tente novamente!");
				}
				
				return true;
			} else {
				player.sendMessage("§cUse /MineCart GerarVIP <player> <grupo> <tempo>");
			}
		}
		
		return false;
	}
}
