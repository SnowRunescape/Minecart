package br.com.minecart.commands;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.minecart.Minecart;
import br.com.minecart.MinecartAPI;
import br.com.minecart.MinecartKey;
import br.com.minecart.utilities.Messaging;

public class MyKeys implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        Player player = (Player) sender;

        try {
            ArrayList<MinecartKey> minecartKeys = MinecartAPI.myKeys(player);

            player.sendMessage(Messaging.format("success.player-list-keys-title", false, true));
            player.sendMessage("");

            if (!minecartKeys.isEmpty()) {
                for (MinecartKey minecartKey : minecartKeys) {
                    String msg = Minecart.instance.ResourceMessage.getString("success.player-list-keys-key");

                    msg = msg.replace("{key.code}", minecartKey.getKey());
                    msg = msg.replace("{key.group}", minecartKey.getGroup());
                    msg = msg.replace("{key.duration}", String.valueOf(minecartKey.getDuration()));

                    player.sendMessage(Messaging.format(msg, false, false));
                }
            } else {
                player.sendMessage(Messaging.format("error.player-dont-have-key", false, true));
            }

            return true;
        } catch(Exception e) {
            player.sendMessage(Messaging.format("error.internal-error", false, true));
        }

        return false;
    }
}