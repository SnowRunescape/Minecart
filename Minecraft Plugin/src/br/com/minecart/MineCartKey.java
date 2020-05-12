package br.com.minecart;

public class MineCartKey {
	private String owner;
	private String group;
	
	private Integer duration;
	
	public MineCartKey(String owner, String group, Integer duration){
		this.owner = owner;
		this.group = group;
		this.duration = duration;
	}
	
	public String getOwner(){
		return this.owner;
	}
	
	public String getGrup(){
		return this.group;
	}
	
	public Integer getDuration(){
		return this.duration;
	}
}
