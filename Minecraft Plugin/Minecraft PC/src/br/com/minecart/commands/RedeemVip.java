package br.com.minecart.commands;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.com.minecart.Minecart;
import br.com.minecart.MinecartKey;
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

        String key = args[0];

        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("username", player.getName());
        params.put("key", key);

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
                MinecartKey minecartKey = new MinecartKey(key, group, duration);

                this.deliverVip(player, minecartKey);
            } else {
                this.processHttpError(player, response);
            }
        } catch(Exception e) {
            player.sendMessage(Messaging.format("error.internal-error", false, true));
        }

        return false;
    }

    private void deliverVip(Player player, MinecartKey minecartKey)
    {
        if (this.executeCommands(minecartKey.getCommands())) {
            this.sendMessageSuccessful(player, minecartKey);
        } else {
            this.sendMessageFailed(player, minecartKey);
        }
    }

    private Boolean executeCommands(String[] commands)
    {
        Boolean result = true;

        for (String command : commands) {
            if (!Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command)) {
                result = false;
            }
        }

        return result;
    }

    private void sendMessageSuccessful(Player player, MinecartKey minecartKey)
    {
        Iterator<String> messages = Minecart.instance.ResourceMessage.getStringList("success.active-key").iterator();

        while (messages.hasNext()) {
            String message = messages.next();

            message = message.replace("{key.group}", minecartKey.getGroup());
            message = message.replace("{key.duration}", String.valueOf(minecartKey.getDuration()));
            message = message.replace("{player.name}", player.getName());

            player.sendMessage(Messaging.format(message, false, false));
        }
    }

    private void sendMessageFailed(Player player, MinecartKey minecartKey)
    {
        String message = Minecart.instance.ResourceMessage.getString("error.redeem-vip");

        message = message.replace("{key.group}", minecartKey.getGroup());
        message = message.replace("{key.duration}", String.valueOf(minecartKey.getDuration()));

        player.sendMessage(Messaging.format("error.internal-error", true, true));
        player.sendMessage(Messaging.format(message, true, false));

        LOGStorage.resgatarVIP("[ERROR] Ocorreu um erro ao ATIVAR o VIP ( "+ minecartKey.getGroup() +" ) com duração de ( " + String.valueOf(minecartKey.getDuration()) + " ) DIAS para o jogador ( " + player.getName() + " ).");
    }

    private void processHttpError(Player player, HttpResponse response)
    {
        if (player.hasPermission("minecart.admin")) {
            if (response.responseCode == 401) {
                player.sendMessage(Messaging.format("error.invalid-shopkey", false, true));
            } else {
                try {
                    JsonParser jsonParser = new JsonParser();

                    JsonObject jsonObject = jsonParser.parse(response.response).getAsJsonObject();

                    Integer errorCode = jsonObject.get("code").getAsInt();

                    if(errorCode == 40010){
                        String kickMessage = Messaging.format("error.invalid-key", true, true);
                        kickMessage = kickMessage.replace("\\n", "\n");

                        player.kickPlayer(kickMessage);
                    } else if(errorCode == 40011){
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