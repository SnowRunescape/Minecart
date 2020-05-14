package br.com.minecart;

public class MineCartKey {
	private String owner;
	private String code;
	private String group;
	
	private Integer duration;
	
	public MineCartKey(String owner, String code, String group, Integer duration){
		this.owner = owner;
		this.code = code;
		this.group = group;
		this.duration = duration;
	}
	
	public String getOwner(){
		return this.owner;
	}
	
	public String getCode(){
		return this.code;
	}
	
	public String getGrup(){
		return this.group;
	}
	
	public Integer getDuration(){
		return this.duration;
	}
}
