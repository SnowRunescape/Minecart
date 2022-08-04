package br.com.minecart;

public class MinecartKey
{
	private String key;
	private String group;
	private int duration;

	public MinecartKey(String key, String group, int duration)
    {
		this.key = key;
		this.group = group;
		this.duration = duration;
	}

	public String getKey()
    {
		return this.key;
	}

	public String getGroup()
    {
		return this.group;
	}

	public int getDuration()
    {
		return this.duration;
	}
}