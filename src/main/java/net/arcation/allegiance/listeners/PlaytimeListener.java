package net.arcation.allegiance.listeners;

import net.arcation.allegiance.Allegiance;
import net.arcation.allegiance.data.PlayerData;
import net.arcation.allegiance.targets.PlaytimeTarget;
import net.arcation.allegiance.util.Hash;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Mr_Little_Kitty on 3/2/2017.
 */
public class PlaytimeListener implements Listener, Runnable
{
	private final PlaytimeTarget target;
	private final Allegiance allegiance;
	//private static final String PLAYTIME_KEY = "P";

	//We're just going to store one int for their previous location
	//If the location they are at at (currentTime + playTimeCheck) has the EXACT hash of the location they are at now
	//AND they are different locations, ... then I don't know what to tell you. Deal with it.
	private final HashMap<UUID,Integer> locationHashes;
	private long lastUpdate;

	public PlaytimeListener(Allegiance allegiance,PlaytimeTarget target, long playTimeCheckInTicks)
	{
		this.allegiance = allegiance;
		this.target = target;

		locationHashes = new HashMap<>();

		//Bukkit.getPluginManager().registerEvents(this,allegiance);
		Bukkit.getScheduler().runTaskTimer(allegiance,this,20*60,playTimeCheckInTicks);
		lastUpdate = System.currentTimeMillis();
	}

	@Override
	public void run()
	{
		allegiance.log("Running scheduled play time check...");

		int incrementAmount = (int)((System.currentTimeMillis()-lastUpdate)/(1000*60)); //increment amount in minutes

		for(Player player : Bukkit.getOnlinePlayers())
		{
			UUID id = player.getUniqueId();
			Location loc = player.getLocation();
			int currentHash = Hash.HashCode(loc.getBlockX(),loc.getBlockY(),loc.getBlockZ());

			if(!locationHashes.containsKey(id))
			{
				locationHashes.put(id,currentHash);
				continue; //Sorry you don't get a playtime increment if you weren't on at the last check
			}

			//Get their old location hash
			int oldHash = locationHashes.get(id);

			//Put in their new location hash
			locationHashes.put(id,currentHash);

			if(currentHash == oldHash)
				continue; //Sorry you don't get a playtime increment because you are in the same location (probably)

			PlayerData data = allegiance.getPlayer(id);
			data.increaseTarget(target,incrementAmount);
		}
		lastUpdate = System.currentTimeMillis();
	}

//	@EventHandler(priority = EventPriority.MONITOR,ignoreCancelled = true)
//	public void onPlayerLeave(PlayerKickEvent event)
//	{
//		handleLeave(event.getPlayer().getUniqueId());
//	}
//
//	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
//	public void onPlayerLeave(PlayerQuitEvent event)
//	{
//		handleLeave(event.getPlayer().getUniqueId());
//	}
//
//	private void handleLeave(UUID id)
//	{
//		if(locationHashes.containsKey(id))
//			locationHashes.remove(id);
//	}
}
