package br.com.minecart.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Minecart implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        if (args.length == 0) {
            this.listCommands(sender);
        } else if (args[0] == "reload") {
            this.reloadConfig();
        }

        return false;
    }

    private void listCommands(CommandSender sender)
    {
        sender.sendMessage("[Minecart] version [§9" + br.com.minecart.Minecart.instance.VERSION + "§f]");
    }

    private void reloadConfig()
    {
        // TODO
    }
}