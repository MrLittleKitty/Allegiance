package net.arcation.allegiance;

import net.arcation.allegiance.commands.AllegianceCommand;
import net.arcation.allegiance.data.DataStorage;
import net.arcation.allegiance.data.PlayerData;
import net.arcation.allegiance.data.ShittyFileStorage;
import net.arcation.allegiance.listeners.NaughtyListeners;
import net.arcation.allegiance.listeners.PlaytimeListener;
import net.arcation.allegiance.listeners.BlockTargetListeners;
import net.arcation.allegiance.targets.BlockTarget;
import net.arcation.allegiance.targets.BlockTargetType;
import net.arcation.allegiance.targets.PlaytimeTarget;
import net.arcation.allegiance.targets.Target;
import net.arcation.allegiance.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

/**
 * Created by Mr_Little_Kitty on 2/24/2017.
 */
public class Allegiance extends JavaPlugin implements Listener
{
	private List<Target> targets;
    private HashMap<UUID,PlayerData> playerCache;
    private DataStorage storage;

    @Override
    public void onEnable()
    {
    	//Register us to load/unload player data
    	Bukkit.getPluginManager().registerEvents(this,this);

        playerCache = new HashMap<>();
		targets = new ArrayList<>();

		ConfigManager manager = new ConfigManager(this);

		manager.checkDefaults();

		PlaytimeTarget playtimeTarget = manager.getPlayTimeTarget();
		if(playtimeTarget != null)
		{
			log("-Loaded new Playtime target set at "+playtimeTarget.getMinutesRequired()+" minutes.");

			int updateTimeInMinutes = manager.getPlayTimeUpdateTime();

			log("-Will be running Playtime updates every "+updateTimeInMinutes+" minutes.");

			new PlaytimeListener(this,playtimeTarget,(long)updateTimeInMinutes*60L*20L);//Time is in ticks
			targets.add(playtimeTarget);
		}

		Map<BlockTargetType,List<BlockTarget>> blockTargets = manager.getBlockTargets();
		if(blockTargets != null)
		{
			BlockTargetListeners blockListeners = new BlockTargetListeners(this);
			for(Map.Entry<BlockTargetType,List<BlockTarget>> entry : blockTargets.entrySet())
			{
				for(BlockTarget target : entry.getValue())
				{
					log("-Loaded new block "+entry.getKey().name()+" target set at "+target.getAmount()+" of "+target.getMaterial().name());
					blockListeners.addBlockTarget(entry.getKey(),target);
					targets.add(target);
				}
			}
		}

		if(targets.isEmpty())
		{
			log("UH-OH! It doesn't look like you have any Allegiance targets. Add some to the config and restart.");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}

		//Initialize listeners to stop people from raiding, pvping, etc
		new NaughtyListeners(this,manager);

		File storageLocation = new File(getDataFolder(),"playerData");
		if(!storageLocation.exists() || !storageLocation.isDirectory())
			storageLocation.mkdir();
		storage = new ShittyFileStorage(storageLocation);

		//Load data for any players online (if they used /reload)
		for(Player player : Bukkit.getOnlinePlayers())
			loadPlayer(player.getUniqueId());

		new AllegianceCommand(this,manager.getDetailedAllegiance());
    }

    @Override
    public void onDisable()
    {
		for(Player player : Bukkit.getOnlinePlayers())
			unloadPlayer(player.getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR,ignoreCancelled = true)
    public void playerJoinLoadData(PlayerJoinEvent event)
    {
		loadPlayer(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR,ignoreCancelled = true)
    public void playerLeaveUnloadData(PlayerQuitEvent event)
    {
		unloadPlayer(event.getPlayer().getUniqueId());
    }

    public Iterator<Pair<UUID,PlayerData>> hOLYFUCK_ThisGetsAllJoinedPlayersDataAndIsIntensive()
    {
        return storage.getAllStroredDataIterator(targets);
    }

    private void loadPlayer(UUID id)
	{
		playerCache.put(id,storage.loadPlayerData(id,targets));
	}

    private void unloadPlayer(UUID id)
	{
		storage.savePlayerData(id,playerCache.get(id));
		playerCache.remove(id);
	}

    public PlayerData getPlayer(UUID id)
    {
        return playerCache.get(id);
    }

    public Set<Map.Entry<UUID,PlayerData>> getOnlinePlayersData()
    {
        return playerCache.entrySet();
    }

    public List<Target> getTargets()
	{
		return targets;
	}

    public void log(String information)
    {
        getLogger().info(information);
    }

    public void sendMessageToPlayer(Player player, String message)
	{
		player.sendMessage(ChatColor.BLUE+"[Allegiance] "+ ChatColor.WHITE+message);
	}

	public void announcePlayerAllegiance(Player player)
	{
		log("Player ["+player.getName()+"] just became allegiant.");

		player.sendMessage(ChatColor.GREEN+"You just became allegiant! Huzzah!");
	}
}
