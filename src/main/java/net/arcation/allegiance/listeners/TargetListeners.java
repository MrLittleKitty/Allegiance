package net.arcation.allegiance.listeners;

import net.arcation.allegiance.Allegiance;
import net.arcation.allegiance.targets.BlockTarget;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

/**
 * Created by Mr_Little_Kitty on 3/2/2017.
 */
public class TargetListeners implements Listener
{
	private Allegiance allegiance;

	public TargetListeners(Allegiance allegiance, List<BlockTarget> breakTargets, List<BlockTarget> placeTargets)
	{
		this.allegiance = allegiance;
		Bukkit.getPluginManager().registerEvents(this,allegiance);
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent event)
	{

	}

	@EventHandler
	public void onBreak(BlockBreakEvent event)
	{

	}
}
