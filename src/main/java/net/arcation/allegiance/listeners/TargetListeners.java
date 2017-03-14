package net.arcation.allegiance.listeners;

import net.arcation.allegiance.Allegiance;
import net.arcation.allegiance.PlayerData;
import net.arcation.allegiance.targets.BlockTarget;
import net.arcation.allegiance.targets.BlockTargetType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

/**
 * Created by Mr_Little_Kitty on 3/2/2017.
 */
public class TargetListeners implements Listener
{
	private Allegiance allegiance;

	//TODO---Currently not possible to have more than one target per block type
	//You cant have different targets for different wool colors etc
	private EnumMap<BlockTargetType,Map<Material,BlockTarget>> targetMap;

	public TargetListeners(Allegiance allegiance)
	{
		this.allegiance = allegiance;
		Bukkit.getPluginManager().registerEvents(this,allegiance);
		targetMap = new EnumMap<BlockTargetType, Map<Material, BlockTarget>>(BlockTargetType.class);
	}

	public void addBlockTarget(BlockTargetType type, BlockTarget target)
	{
		//Woah lambdas are cool
		Map<Material, BlockTarget> map = targetMap.computeIfAbsent(type, k -> new HashMap<>());
		map.put(target.getMaterial(),target);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlace(BlockPlaceEvent event)
	{
		checkEvent(BlockTargetType.PLACE,event.getBlockPlaced(),event.getPlayer().getUniqueId());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onBreak(BlockBreakEvent event)
	{
		checkEvent(BlockTargetType.BREAK,event.getBlock(),event.getPlayer().getUniqueId());
	}

	private void checkEvent(BlockTargetType type, Block block, UUID uuid)
	{
		Map<Material,BlockTarget> map = targetMap.get(type);
		if(map != null)
		{
			BlockTarget target = map.get(block.getType());
			if(target != null)
			{
				PlayerData data = allegiance.getPlayer(uuid);
				data.increaseTarget(target,1);
			}
		}
	}
}
