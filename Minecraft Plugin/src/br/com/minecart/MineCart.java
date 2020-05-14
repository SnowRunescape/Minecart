package br.com.minecart;

import java.io.File;
import java.sql.SQLException;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import br.com.minecart.commands.MainCommand;
import br.com.minecart.database.Database;

public class MineCart extends JavaPlugin {
	public YamlConfiguration ResourceMessage;
	public MineCartKeyManage keysManage;
	public Database database;
	
	public static MineCart instance;
	
	public String MineCartAutorization;
	public String MineCartServerToken;
	
	public final String MineCartAPI = "https://minecart.com.br/api";
	
	public void onEnable(){
		instance = this;
		
		if(!this.setupDatabase()){
			Bukkit.getPluginManager().disablePlugin(this);
			
			return;
		}
		
		this.keysManage = new MineCartKeyManage();
		
		if(!new File(getDataFolder(), "config.yml").exists()) saveDefaultConfig();
		
		this.loadMessages();
		
		this.MineCartAutorization = getConfig().getString("MineCart.ShopKey", "");
		this.MineCartServerToken = getConfig().getString("MineCart.ShopServerKey", "");
		
		getCommand("resgatarvip").setExecutor(new MainCommand());
		getCommand("resgatarcash").setExecutor(new MainCommand());
		getCommand("minhaskeys").setExecutor(new MainCommand());
		getCommand("ativar").setExecutor(new MainCommand());
		getCommand("minecart").setExecutor(new MainCommand());
	}
	
	public static Database getDB() {
		return instance.database;
	}
	
    private boolean setupDatabase(){
        try {
            database = new Database(getConfig().getConfigurationSection("database"));
        } catch (ClassNotFoundException exception){
            getLogger().log(Level.SEVERE, String.format("Unable to register JDCB driver: %s", exception.getMessage()));
            return false;
        } catch (SQLException exception){
            getLogger().log(Level.SEVERE, String.format("Unable to connect to SQL server: %s", exception.getMessage()));
            return false;
        }

        try {
            database.createTables();
        } catch (Exception exception){
            getLogger().log(Level.SEVERE, String.format("An exception was thrown while attempting to create tables: %s", exception.getMessage()));
            return false;
        }

        return true;
    }
    
    private void loadMessages(){
    	File resourceMessage = new File(getDataFolder(), "messages.yml");
    	
    	if(!resourceMessage.exists()) saveResource("messages.yml", false);
    	
    	this.ResourceMessage = YamlConfiguration.loadConfiguration(resourceMessage);
    }
}
