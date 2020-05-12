package br.com.minecart;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.entity.Player;

import br.com.minecart.storage.SQLStorage;

public class MineCartKeyManage {
	private HashMap<String, MineCartKey> minecartKeys = new HashMap<String, MineCartKey>();
	
	private SQLStorage SQLStorage = new SQLStorage();
	
	public MineCartKey getKey(String key){
		if(minecartKeys.containsKey(key)) return minecartKeys.get(key);
		
		return null;
	}
	
	public void useKey(Player player, String key){
		
	}
	
	public void newKey(String owner, String group, Integer duration){
		MineCartKey key = new MineCartKey(owner, group, duration);
		
		if(this.SQLStorage.saveMineCartKey(key)){
			minecartKeys.put(this.GEN_RANDOM_KEY(), key);
		}
	}
	
	private String GEN_RANDOM_KEY(){
		Random gerador = new Random();
		
		String key = "";
		
		while(minecartKeys.containsKey(key)){
			key = "";
			
			for(int i = 0; i < 10; i++) key += gerador.nextInt(10);
		}
		
		return key;
	}
}
