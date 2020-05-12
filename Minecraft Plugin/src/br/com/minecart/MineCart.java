package br.com.minecart;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

import br.com.minecart.commands.MainCommand;
import br.com.minecart.database.Database;

public class MineCart extends JavaPlugin {
	public HashMap<String, String> ResourceMessage =  new HashMap<String, String>();
	
	public MineCartKeyManage keysManage = new MineCartKeyManage();
	public Database database;
	
	public static MineCart instance;
	
	public String MineCartAutorization = "MineCart.TEDSADAdadsad1D5d15D1d8as1d8D18d18Dd";
	public String MineCartServerToken;
	
	public final String MineCartAPI = "https://minecart.com.br/api";
	
	public void onEnable(){
		instance = this;
		
		this.getCommand("ResgatarVip").setExecutor(new MainCommand());
		this.getCommand("ResgatarCash").setExecutor(new MainCommand());
		this.getCommand("MinhasKeys").setExecutor(new MainCommand());
		this.getCommand("Ativar").setExecutor(new MainCommand());
	}
	
	public static Database getDB() {
		return instance.database;
	}
	
    private boolean setupDatabase(){
        try {
            database = new Database(getConfig().getConfigurationSection("database"));
        } catch (ClassNotFoundException exception) {
            getLogger().log(Level.SEVERE, String.format("Unable to register JDCB driver: %s", exception.getMessage()));
            return false;
        } catch (SQLException exception) {
            getLogger().log(Level.SEVERE, String.format("Unable to connect to SQL server: %s", exception.getMessage()));
            return false;
        }

        try {
            database.createTables();
        } catch (Exception exception) {
            getLogger().log(Level.SEVERE, String.format("An exception was thrown while attempting to create tables: %s", exception.getMessage()));
            return false;
        }

        return true;
    }
}
