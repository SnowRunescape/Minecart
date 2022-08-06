package br.com.minecart.commands;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.com.minecart.Minecart;
import br.com.minecart.MinecartKey;
import br.com.minecart.utilities.HttpRequest;
import br.com.minecart.utilities.HttpResponse;
import br.com.minecart.utilities.Messaging;

public class MyKeys implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        Player player = (Player) sender;

        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("username", player.getName());

        HttpResponse response = HttpRequest.httpRequest(Minecart.instance.MINECART_API + "/shop/player/mykeys", params);

        if (response == null) {
            player.sendMessage(Messaging.format("error.internal-error", false, true));

            return false;
        }

        try {
            JsonParser jsonParser = new JsonParser();

            JsonObject jsonObject = jsonParser.parse(response.response).getAsJsonObject();

            if (response.responseCode == 200) {
                ArrayList<MinecartKey> minecartKeys = new ArrayList<MinecartKey>();

                JsonArray productsPlayer = jsonObject.getAsJsonArray("products");

                for (JsonElement product : productsPlayer) {
                    JsonObject productObj = product.getAsJsonObject();

                    String key = productObj.get("key").getAsString();
                    String group = productObj.get("group").getAsString();
                    Integer duration = productObj.get("duration").getAsInt();

                    minecartKeys.add(new MinecartKey(key, group, duration, null));
                }

                player.sendMessage(Messaging.format("success.player-list-keys-title", false, true));
                player.sendMessage("");

                if (!minecartKeys.isEmpty()) {
                    for(MinecartKey minecartKey : minecartKeys){
                        String msg = Minecart.instance.ResourceMessage.getString("success.player-list-keys-key");

                        msg = msg.replace("{key.code}", minecartKey.getKey());
                        msg = msg.replace("{key.group}", minecartKey.getGroup());
                        msg = msg.replace("{key.duration}", String.valueOf(minecartKey.getDuration()));

                        player.sendMessage(Messaging.format(msg, false, false));
                    }
                } else {
                    player.sendMessage(Messaging.format("error.player-dont-have-key", false, true));
                }
            } else {
                this.processHttpError(player, response);
            }
        } catch(Exception e) {
            player.sendMessage(Messaging.format("error.internal-error", false, true));
        }

        return false;
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

                    if (errorCode == 40011) {
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