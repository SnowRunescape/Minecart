package br.com.minecart;

public class MinecartKey
{
    private String key;
    private String group;
    private int duration;
    private String[] commands;

    public MinecartKey(String key, String group, int duration, String[] commands)
    {
        this.key = key;
        this.group = group;
        this.duration = duration;
        this.commands = commands;
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

    public String[] getCommands()
    {
        return this.commands;
    }
}