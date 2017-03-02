package net.arcation.allegiance;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Mr_Little_Kitty on 2/24/2017.
 */
public class Allegiance extends JavaPlugin
{
    private HashMap<UUID,PlayerData> playerCache;

    @Override
    public void onEnable()
    {
        playerCache = new HashMap<>();
    }

    @Override
    public void onDisable()
    {

    }

    public PlayerData getPlayer(UUID id)
    {
        return playerCache.get(id);
    }

    public void log(String information)
    {

    }
}
