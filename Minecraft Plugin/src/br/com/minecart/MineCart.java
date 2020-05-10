package br.com.minecart;

import java.util.HashMap;

import org.bukkit.plugin.java.JavaPlugin;

import br.com.minecart.commands.MainCommand;

public class MineCart extends JavaPlugin {
	private HashMap<String, String> playersKeys = new HashMap<String, String>();
	
	public HashMap<String, String> ResourceMessage =  new HashMap<String, String>();
	
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
	
	public String CREATE_KEY_VIP(){
		
		return null;
	}
}
