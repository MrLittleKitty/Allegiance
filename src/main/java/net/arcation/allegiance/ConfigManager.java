package net.arcation.allegiance;

import net.arcation.allegiance.targets.BlockTarget;
import net.arcation.allegiance.targets.BlockTargetType;
import net.arcation.allegiance.targets.PlaytimeTarget;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mr_Little_Kitty on 3/15/2017.
 */
public class ConfigManager
{
	private final FileConfiguration config;
	private final JavaPlugin plugin;

	ConfigManager(JavaPlugin plugin)
	{
		this.plugin = plugin;
		this.config = plugin.getConfig();
	}

	public int getAllowFirePercent()
	{
		return 100; //TODO---implement
	}

	public int getAllowPvpPercent()
	{
		return 0; ///TODO---implement
	}

	public int getAllowInventoryBreakingPercent()
	{
		return 0; //TODO----implement
	}

	public int getAllowBastionDamagePercent()
	{
		return 0; //TODO---implementS
	}

	int getPlayTimeUpdateTime()
	{
		return config.getInt("PlaytimeUpdateInMinutes");
	}

	PlaytimeTarget getPlayTimeTarget()
	{
		ConfigurationSection playTimeSec = config.getConfigurationSection("playTimeTarget");
		if(playTimeSec == null)
		{
			createDefaultPlayTimeTarget();
			return null;
		}
		boolean on = playTimeSec.getBoolean("On");
		if(!on)
			return null;

		int id = playTimeSec.getInt("UniqueId");
		int amount = playTimeSec.getInt("TimeInMinutes");
		return new PlaytimeTarget(id,amount);
	}

	Map<BlockTargetType,List<BlockTarget>> getBlockTargets()
	{
		ConfigurationSection blockTargets = config.getConfigurationSection("blockTargets");
		if(blockTargets == null)
		{
			createDefaultBlockTargets();
			return null;
		}

		EnumMap<BlockTargetType,List<BlockTarget>> targets = new EnumMap<BlockTargetType, List<BlockTarget>>(BlockTargetType.class);
		for(BlockTargetType t : BlockTargetType.values())
			targets.put(t,new ArrayList<>());

		for(String key : blockTargets.getKeys(false))
		{
			ConfigurationSection targetSec = blockTargets.getConfigurationSection(key);
			boolean on = targetSec.getBoolean("On");
			if(!on)
				continue;

			int id = targetSec.getInt("UniqueId");
			BlockTargetType type = BlockTargetType.valueOf(targetSec.getString("TargetType"));
			Material material = Material.valueOf(targetSec.getString("Material"));
			int data = targetSec.getInt("Data");
			int amount = targetSec.getInt("Amount");

			if(data > 0)
				targets.get(type).add(new BlockTarget(id,amount,material,(byte)data));
			else
				targets.get(type).add(new BlockTarget(id,amount,material));
		}

		return targets;
	}

	private void createDefaultPlayTimeTarget()
	{
		ConfigurationSection section = config.createSection("playTimeTarget");
		section.set("On",false);
		section.set("UniqueId",1);
		section.set("TimeInMinutes",60*10);
		plugin.saveConfig();
	}

	private void createDefaultBlockTargets()
	{
		ConfigurationSection section = config.createSection("blockTargets");

		ConfigurationSection first = section.createSection("break1");

		first.set("On", false);
		first.set("UniqueId",2);
		first.set("TargetType",BlockTargetType.BREAK.name());
		first.set("Material", Material.STONE.name());
		first.set("Data", -1);
		first.set("Amount", 64*9);

		ConfigurationSection second = section.createSection("place1");

		second.set("On",false);
		second.set("UniqueId",3);
		second.set("TargetType",BlockTargetType.PLACE.name());
		second.set("Material", Material.LOG.name());
		second.set("Data", -1);
		second.set("Amount", 64);

		plugin.saveConfig();
	}
}
