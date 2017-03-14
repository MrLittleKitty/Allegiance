package net.arcation.allegiance;

import net.arcation.allegiance.data.DataStorage;
import net.arcation.allegiance.targets.BlockTarget;
import net.arcation.allegiance.targets.Target;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by Mr_Little_Kitty on 2/24/2017.
 */
public class Allegiance extends JavaPlugin implements Listener
{
    private HashMap<UUID,PlayerData> playerCache;
    private DataStorage storage;

    @Override
    public void onEnable()
    {
        playerCache = new HashMap<>();
    }

    @Override
    public void onDisable()
    {

    }

    @EventHandler(priority = EventPriority.LOWEST,ignoreCancelled = true)
    public void playerJoinLoadData(PlayerJoinEvent event)
    {

    }

    @EventHandler(priority = EventPriority.MONITOR,ignoreCancelled = true)
    public void playerLeaveUnloadData(PlayerQuitEvent event)
    {

    }

    @EventHandler(priority = EventPriority.MONITOR,ignoreCancelled = true)
    public void playerLeaveUnloadData(PlayerKickEvent event)
    {

    }

    public PlayerData getPlayer(UUID id)
    {
        return playerCache.get(id);
    }

    public void log(String information)
    {
        getLogger().info(information);
    }

    private List<Target> loadTargets()
    {
        ArrayList<Target> targets = new ArrayList<>();
        FileConfiguration config = this.getConfig();

        ConfigurationSection targetSec = config.getConfigurationSection("targets");
        if(targetSec != null)
        {
            ConfigurationSection breakTargets = targetSec.getConfigurationSection("break");
            if(breakTargets != null)
            {
                for(String key : breakTargets.getKeys(false))
                {
                    BlockTarget t = loadBlockTarget(breakTargets.getConfigurationSection(key));

                }
            }
            ConfigurationSection placeTargets = targetSec.getConfigurationSection("place");
        }
        return null;//TOOD---Remove
    }

    private BlockTarget loadBlockTarget(ConfigurationSection section)
    {
        try
        {
            int id = section.getInt("id");
            int amount = section.getInt("amount");
            Material material = Material.getMaterial(section.getString("material"));
            byte data = (byte) section.getInt("data");
            return new BlockTarget(id,amount,material,data);
        }
        catch(Exception e)
        {
            log("There was a problem loading a block target from path:"+section.getCurrentPath());
            e.printStackTrace();
            return null;
        }
    }
}
