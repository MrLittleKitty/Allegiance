package net.arcation.allegiance.listeners;

import net.arcation.allegiance.Allegiance;
import net.arcation.allegiance.targets.PlaytimeTarget;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Mr_Little_Kitty on 3/2/2017.
 */
public class PlaytimeListener implements Listener
{
	private final PlaytimeTarget target;

	public PlaytimeListener(Allegiance allegiance,PlaytimeTarget target)
	{
		Bukkit.getPluginManager().registerEvents(this,allegiance);
		this.target = target;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event)
	{

	}

	@EventHandler
	public void onLeave(PlayerQuitEvent event)
	{
		handleLeave(event.getPlayer());
	}

	@EventHandler
	public void onLeave(PlayerKickEvent event)
	{
		handleLeave(event.getPlayer());
	}

	private void handleLeave(Player player)
	{

	}
}
