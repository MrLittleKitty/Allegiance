package net.arcation.allegiance.listeners;

import net.arcation.allegiance.Allegiance;
import net.arcation.allegiance.PlayerData;
import net.arcation.allegiance.targets.PlaytimeTarget;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

/**
 * Created by Mr_Little_Kitty on 3/2/2017.
 */
public class PlaytimeListener implements Listener, Runnable
{
	private final PlaytimeTarget target;
	private final Allegiance allegiance;
	private static final String PLAYTIME_KEY = "P";

	public PlaytimeListener(Allegiance allegiance,PlaytimeTarget target)
	{
		this.allegiance = allegiance;
		Bukkit.getPluginManager().registerEvents(this,allegiance);
		this.target = target;
	}

	@EventHandler(priority = EventPriority.MONITOR,ignoreCancelled = true)
	public void onJoin(PlayerJoinEvent event)
	{
		PlayerData d = allegiance.getPlayer(event.getPlayer().getUniqueId());
		d.setData(PLAYTIME_KEY,System.currentTimeMillis());
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onLeave(PlayerQuitEvent event)
	{
		handleLeave(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onLeave(PlayerKickEvent event)
	{
		handleLeave(event.getPlayer());
	}

	private void handleLeave(Player player)
	{
		PlayerData data = allegiance.getPlayer(player.getUniqueId());
		long playTime = System.currentTimeMillis() - (long)data.getData(PLAYTIME_KEY);

		data.increaseTarget(target,(int)(playTime/1000));
	}

	@Override
	public void run()
	{

	}
}
