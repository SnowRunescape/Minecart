package br.com.minecart.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Minecart implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        if (args.length == 0) {
            return this.listCommands(sender);
        } else if (args[0].equalsIgnoreCase("reload")) {
            return this.reloadConfig(sender);
        }

        sender.sendMessage("§9[Minecart] §cComando Inexistente");

        return false;
    }

    private boolean listCommands(CommandSender sender)
    {
        sender.sendMessage("§9[Minecart] §fVersion [§c" + br.com.minecart.Minecart.instance.VERSION + "§f]");

        if ((sender instanceof Player) && sender.hasPermission("minecart.admin")) {
            sender.sendMessage("");
            sender.sendMessage("§b/Minecart reload §f- Recarregar configurações");
        }

        return true;
    }

    private boolean reloadConfig(CommandSender sender)
    {
        sender.sendMessage("§9[Minecart] §fConfigurações recarregadas com sucesso");

        br.com.minecart.Minecart.instance.reloadConfig();

        return true;
    }
}