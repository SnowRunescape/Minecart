package br.com.minecart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import br.com.minecart.storage.LOGStorage;
import br.com.minecart.storage.SQLStorage;
import br.com.minecart.utilities.Messaging;

public class MineCartKeyManage {
	private HashMap<String, MineCartKey> minecartKeys = new HashMap<String, MineCartKey>();
	
	private SQLStorage SQLStorage = new SQLStorage();
	
	public MineCartKeyManage(){
		this.loadMineCartKeys();
	}
	
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
			
			if(this.SQLStorage.deleteMineCartKey(minecartKey)){
				if(Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd)){
					this.minecartKeys.remove(key);
					
					return true;
				} else {
					if(!this.SQLStorage.saveMineCartKey(minecartKey)){
						this.minecartKeys.remove(key);
						
						String msg = MineCart.instance.ResourceMessage.getString("error.redeem-vip");
						
						msg = msg.replace("{key.group}", minecartKey.getGrup());
						msg = msg.replace("{key.duration}", String.valueOf(minecartKey.getDuration()));
						
						player.sendMessage(Messaging.format("error.internal-error", true, true));
						player.sendMessage(Messaging.format(msg, true, false));
						
						LOGStorage.resgatarVIP("[ERROR] Ocorreu um erro ao ATIVAR o VIP ( "+ minecartKey.getGrup() +" ) com duração de ( " + String.valueOf(minecartKey.getDuration()) + " ) DIAS para o jogador ( " + player.getName() + " ).");
					}
				}
			}
		}
		
		return false;
	}
	
	public Boolean newKey(String owner, String group, Integer duration){
		String keyCode = this.GEN_RANDOM_KEY();
		
		owner = owner.toLowerCase();
		
		MineCartKey key = new MineCartKey(owner, keyCode, group, duration);
		
		if(this.SQLStorage.saveMineCartKey(key)){
			minecartKeys.put(keyCode, key);
			
			return true;
		}
		
		return false;
	}
	
	public void loadMineCartKeys(){
		this.minecartKeys = this.SQLStorage.loadAllMineCartKeys();
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
