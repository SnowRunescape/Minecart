package br.com.minecart;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Cooldown {
	private HashMap<String, Long> cooldown = new HashMap<String, Long>();
	
	public Boolean inCooldown(String commandLabel){
		if(cooldown.containsKey(commandLabel)){
			if(System.currentTimeMillis() < cooldown.get(commandLabel)){
				return true;
			}
		}
		
		return false;
	}
	
	public void addCooldown(String commandLabel, int time){
		cooldown.put(commandLabel, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(time));
	}
}
