package br.com.minecart.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.minecart.Minecart;
import br.com.minecart.MinecartAPI;
import br.com.minecart.storage.LOGStorage;
import br.com.minecart.utilities.HttpRequestException;
import br.com.minecart.utilities.Messaging;

public class RedeemCash implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        Player player = (Player) sender;

        try {
            int quantity= MinecartAPI.redeemCash(player);

            if (quantity > 0) {
                this.deliverCash(player, quantity);
            }
        } catch(HttpRequestException e) {
            MinecartAPI.processHttpError(player, e.getResponse());
        }

        return false;
    }

    private void deliverCash(Player player, int quantity)
    {
        String cmdTemp = Minecart.instance.getConfig().getString("cmd.cmd_active_cash");

        cmdTemp = cmdTemp.replace("{player.name}", player.getName());
        cmdTemp = cmdTemp.replace("{cash.quantity}", String.valueOf(quantity));

        if (Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmdTemp)) {
            String msg = Minecart.instance.ResourceMessage.getString("success.redeem-cash");

            msg = msg.replace("{player.name}", player.getName());

            player.sendMessage(Messaging.format(msg, true, false));
        } else {
            String msg = Minecart.instance.ResourceMessage.getString("error.redeem-cash");

            msg = msg.replace("{cash.amount}", String.valueOf(quantity));

            player.sendMessage(Messaging.format("error.internal-error", true, true));
            player.sendMessage(Messaging.format(msg, true, false));

            LOGStorage.resgatarCASH("[ERROR] Ocorreu um erro ao dar ( "+ String.valueOf(quantity) +" ) de CASH para o jogador ( " + player.getName() + " ).");
        }
    }
}