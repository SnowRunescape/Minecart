package br.com.minecart.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.Maps;

import br.com.minecart.Cooldown;
import br.com.minecart.utilities.Messaging;

public class MainCommand implements CommandExecutor
{
    private static final  int COOLDOWN = 5;

    private static HashMap<Player, Cooldown> cooldown = new HashMap<Player, Cooldown>();

    private static Map<String, CommandExecutor> CommandMap = Maps.newHashMap();

    public MainCommand()
    {
        CommandMap.put("minecart", new Minecart());

        CommandMap.put("mykeys", new MyKeys());
        CommandMap.put("minhaskeys", new MyKeys());

        CommandMap.put("redeemcash", new RedeemCash());
        CommandMap.put("resgatarcash", new RedeemCash());

        CommandMap.put("redeemvip", new RedeemVip());
        CommandMap.put("resgatarvip", new RedeemVip());
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messaging.format("error.player-only", true, true));
            return false;
        }

        Player player = (Player) sender;
        commandLabel = commandLabel.toLowerCase();

        if (CommandMap.containsKey(commandLabel)) {
            CommandExecutor command = CommandMap.get(commandLabel);

            if (!hasPermission(player, command)) {
                player.sendMessage(Messaging.format("error.insufficient-permissions", true, true));
                return false;
            }

            if (this.inCooldown(player, commandLabel)) {
                player.sendMessage(Messaging.format("error.cooldown", true, true));
                return false;
            } else {
                this.addCooldown(player, commandLabel);

                return command.onCommand(sender, cmd, commandLabel, args);
            }
        }

        return true;
    }

    private boolean hasPermission(Player bukkitPlayer, CommandExecutor cmd)
    {
        CommandPermissions permissions = cmd.getClass().getAnnotation(CommandPermissions.class);

        if (permissions == null) {
            return true;
        }

        for (String permission : permissions.value()) {
            if (bukkitPlayer.hasPermission(permission)) {
                return true;
            }
        }

        return false;
    }

    private boolean inCooldown(Player player, String commandLabel)
    {
        if (cooldown.containsKey(player)) {
            Cooldown Cooldown = cooldown.get(player);

            return Cooldown.inCooldown(commandLabel);
        }

        return false;
    }

    private void addCooldown(Player player, String commandLabel)
    {
        if (!cooldown.containsKey(player)) {
            cooldown.put(player, new Cooldown());
        }

        Cooldown Cooldown = cooldown.get(player);

        Cooldown.addCooldown(commandLabel, COOLDOWN);
    }
}