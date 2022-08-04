package br.com.minecart.commands;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.com.minecart.Minecart;
import br.com.minecart.storage.LOGStorage;
import br.com.minecart.utilities.HttpRequest;
import br.com.minecart.utilities.HttpResponse;
import br.com.minecart.utilities.Messaging;
import br.com.minecart.utilities.Utils;

public class RedeemVip implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(Messaging.format("error.inform-key", true, true));

            return false;
        }

        if (Minecart.instance.getConfig().getBoolean("config.force_clean_inventry", true) && Utils.playerInventoryClean(player)) {
            player.sendMessage(Messaging.format("error.clean-inventory", true, true));

            return false;
        }

        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("username", player.getName());
        params.put("key", args[0]);

        HttpResponse response = HttpRequest.httpRequest(Minecart.instance.MINECART_API + "/shop/player/redeemvip", params);

        if (response == null) {
            player.sendMessage(Messaging.format("error.internal-error", false, true));

            return false;
        }

        try {
            JsonParser JsonParser = new JsonParser();

            JsonObject jsonObject = JsonParser.parse(response.response).getAsJsonObject();

            if (response.responseCode == 200) {
                String group = jsonObject.get("group").getAsString();
                Integer duration = jsonObject.get("duration").getAsInt();

                this.deliverVip(player, group, duration);
            } else {
                this.processHttpError(player, response);
            }
        } catch(Exception e) {
            player.sendMessage(Messaging.format("error.internal-error", false, true));
        }

        return false;
    }

    private void deliverVip(Player player, String group, int duration)
    {
        String cmd = Minecart.instance.getConfig().getString("cmd.cmd_active_vip");

        cmd = cmd.replace("{key.group}", group);
        cmd = cmd.replace("{key.duration}", String.valueOf(duration));
        cmd = cmd.replace("{player.name}", player.getName());

        if (Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd)) {
            Iterator<String> msg = Minecart.instance.ResourceMessage.getStringList("success.active-key").iterator();

            while (msg.hasNext()) {
                String msgTemp = (String) msg.next();

                msgTemp = msgTemp.replace("{key.group}", group);
                msgTemp = msgTemp.replace("{key.duration}", String.valueOf(duration));
                msgTemp = msgTemp.replace("{player.name}", player.getName());

                player.sendMessage(Messaging.format(msgTemp, false, false));
            }
        } else {
            String msg = Minecart.instance.ResourceMessage.getString("error.redeem-vip");

            msg = msg.replace("{key.group}", group);
            msg = msg.replace("{key.duration}", String.valueOf(duration));

            player.sendMessage(Messaging.format("error.internal-error", true, true));
            player.sendMessage(Messaging.format(msg, true, false));

            LOGStorage.resgatarVIP("[ERROR] Ocorreu um erro ao ATIVAR o VIP ( "+ group +" ) com duração de ( " + String.valueOf(duration) + " ) DIAS para o jogador ( " + player.getName() + " ).");
        }
    }

    private void processHttpError(Player player, HttpResponse response)
    {
        if (player.hasPermission("minecart.admin")) {
            if (response.responseCode == 401) {
                player.sendMessage(Messaging.format("error.invalid-shopkey", false, true));
            } else {
                try {
                    JsonParser JsonParser = new JsonParser();

                    JsonObject jsonObject = JsonParser.parse(response.response).getAsJsonObject();

                    Integer error_code = jsonObject.get("code").getAsInt();

                    if(error_code == 40010){
                        String kickMessage = Messaging.format("error.invalid-key", true, true);
                        kickMessage = kickMessage.replace("\\n", "\n");

                        player.kickPlayer(kickMessage);
                    } else if(error_code == 40011){
                        player.sendMessage(Messaging.format("error.invalid-shopserver", false, true));
                    } else {
                        player.sendMessage(Messaging.format("error.internal-error", false, true));
                    }
                } catch(Exception e) {
                    player.sendMessage(Messaging.format("error.internal-error", false, true));
                }
            }
        } else {
            player.sendMessage(Messaging.format("error.internal-error", false, true));
        }
    }
}