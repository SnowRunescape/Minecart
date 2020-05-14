package br.com.minecart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import br.com.minecart.storage.SQLStorage;

public class MineCartKeyManage {
	private HashMap<String, MineCartKey> minecartKeys = new HashMap<String, MineCartKey>();
	
	private SQLStorage SQLStorage = new SQLStorage();
	
	public MineCartKey getKey(String key){
		if(minecartKeys.containsKey(key)) return minecartKeys.get(key);
		
		return null;
	}
	
	public ArrayList<String> getPlayerKeys(Player player){
		ArrayList<String> keys = new ArrayList<String>();
		
		for(Entry<String, MineCartKey> minecartKey : minecartKeys.entrySet()){
			if(player.getName().equalsIgnoreCase(minecartKey.getValue().getOwner())){
				keys.add(minecartKey.getKey());
			}
		}
		
		return keys;
	}
	
	public Boolean useKey(Player player, String key){
		String cmd = MineCart.instance.getConfig().getString("cmd.cmd_active_vip");
		
		MineCartKey minecartKey = this.getKey(key);
		
		if(minecartKey != null){
			cmd = cmd.replace("{key.group}", minecartKey.getGrup());
			cmd = cmd.replace("{key.duration}", String.valueOf(minecartKey.getDuration()));
			cmd = cmd.replace("{player.name}", player.getName());
			
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
			
			this.minecartKeys.remove(key);
			
			return true;
		}
		
		return false;
	}
	
	public Boolean newKey(String owner, String group, Integer duration){
		String keyCode = this.GEN_RANDOM_KEY();
		
		MineCartKey key = new MineCartKey(owner, keyCode, group, duration);
		
		if(this.SQLStorage.saveMineCartKey(key)){
			minecartKeys.put(keyCode, key);
			
			return true;
		}
		
		return false;
	}
	
	private String GEN_RANDOM_KEY(){
		Random gerador = new Random();
		
		String key = "";
		
		while((!minecartKeys.containsKey(key)) && (key == "")){
			key = "";
			
			for(int i = 0; i < 10; i++) key += gerador.nextInt(10);
		}
		
		return key;
	}
}
