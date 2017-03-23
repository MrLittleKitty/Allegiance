package net.arcation.allegiance;

import net.arcation.allegiance.targets.BlockTarget;
import net.arcation.allegiance.targets.BlockTargetType;
import net.arcation.allegiance.targets.PlaytimeTarget;
import org.bukkit.Material;
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
		return getOrSetDefault("PercentToLightShitOnFire",100);
	}

	public int getAllowPvpPercent()
	{
		return getOrSetDefault("PercentToPVP",100);
	}

	public int getFightTimer()
	{
		return getOrSetDefault("HowLongPlayerCanFightAfterBeingAttackedInSeconds",30);
	}

	public boolean getTagOnReinforcementDamage()
	{
		return getOrSetDefault("MakeFightableWhenDamagingReinforcements",true);
	}

	public int getAllowReinforcementDamagePercent()
	{
		return getOrSetDefault("PercentToDamageReinforcements",100);
	}

	public int getAllowBastionDamagePercent()
	{
		return getOrSetDefault("PercentToDamageBastions",100);
	}

	boolean getDetailedAllegiance()
	{
		return getOrSetDefault("DetailedAllegianceInfo",false);
	}

	int getPlayTimeUpdateTime()
	{
		return getOrSetDefault("PlaytimeUpdateInMinutes",2);
	}

	private boolean getOrSetDefault(String path, boolean defaultValue)
	{
		if(!config.isSet(path))
		{
			config.set(path,defaultValue);
			plugin.saveConfig();
		}
		return config.getBoolean(path);
	}

	private int getOrSetDefault(String path, int defaultValue)
	{
		if(!config.isSet(path))
		{
			config.set(path,defaultValue);
			plugin.saveConfig();
		}
		return config.getInt(path);
	}

	void checkDefaults()
	{
		getAllowFirePercent();
		getAllowPvpPercent();
		getAllowReinforcementDamagePercent();
		getAllowBastionDamagePercent();
		getPlayTimeUpdateTime();
		getDetailedAllegiance();
		getFightTimer();
		getTagOnReinforcementDamage();
	}

	PlaytimeTarget getPlayTimeTarget()
	{
		ConfigurationSection playTimeSec = config.getConfigurationSection("playTimeTarget");
		if(playTimeSec == null)
		{
			createDefaultPlayTimeTarget();
			return null;
		}
		try
		{
			int id = playTimeSec.getInt("UniqueId");
			int amount = playTimeSec.getInt("TimeInMinutes");
			return new PlaytimeTarget(id,amount);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
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


		for (String key : blockTargets.getKeys(false))
		{
			try
			{
				ConfigurationSection targetSec = blockTargets.getConfigurationSection(key);

				int id = targetSec.getInt("UniqueId");
				BlockTargetType type = BlockTargetType.valueOf(targetSec.getString("TargetType"));

				Material material;
				String materialString = targetSec.getString("Material");
				if (materialString.equalsIgnoreCase("all"))
					material = Material.AIR;
				else
					material = Material.valueOf(materialString);

				int data = targetSec.getInt("Data");
				int amount = targetSec.getInt("Amount");

				if (data > 0)
					targets.get(type).add(new BlockTarget(id, type, amount, material, (byte) data));
				else
					targets.get(type).add(new BlockTarget(id, type, amount, material));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return targets;
	}

	private void createDefaultPlayTimeTarget()
	{
		ConfigurationSection section = config.createSection("playTimeTarget");
		section.set("UniqueId",1);
		section.set("TimeInMinutes",60*10); //10 hours
		plugin.saveConfig();
	}

	private void createDefaultBlockTargets()
	{
		ConfigurationSection section = config.createSection("blockTargets");

		ConfigurationSection first = section.createSection("break1");

		first.set("UniqueId",2);
		first.set("TargetType",BlockTargetType.BREAK.name());
		first.set("Material", Material.STONE.name());
		first.set("Data", -1);
		first.set("Amount", 64*9);

		ConfigurationSection second = section.createSection("place1");

		second.set("UniqueId",3);
		second.set("TargetType",BlockTargetType.PLACE.name());
		second.set("Material", Material.LOG.name());
		second.set("Data", -1);
		second.set("Amount", 64);

		plugin.saveConfig();
	}
}
