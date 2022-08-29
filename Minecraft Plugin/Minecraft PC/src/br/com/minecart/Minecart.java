package br.com.minecart;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import br.com.minecart.commands.MainCommand;

public class Minecart extends JavaPlugin
{
    public final String VERSION = "2.1.0";

    public YamlConfiguration ResourceMessage;

    public static Minecart instance;

    public String MinecartAutorization;
    public String MinecartShopServer;

    public void onEnable()
    {
        instance = this;

        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }

        this.loadMessages();

        this.MinecartAutorization = getConfig().getString("Minecart.ShopKey", "");
        this.MinecartShopServer = getConfig().getString("Minecart.ShopServer", "");

        MainCommand MainCommand = new MainCommand();

        getCommand("minecart").setExecutor(MainCommand);
        getCommand("mykeys").setExecutor(MainCommand);
        getCommand("redeemcash").setExecutor(MainCommand);
        getCommand("redeemvip").setExecutor(MainCommand);
    }

    private void loadMessages()
    {
        File resourceMessage = new File(getDataFolder(), "messages.yml");

        if (!resourceMessage.exists()) {
            saveResource("messages.yml", false);
        }

        this.ResourceMessage = YamlConfiguration.loadConfiguration(resourceMessage);
    }
}