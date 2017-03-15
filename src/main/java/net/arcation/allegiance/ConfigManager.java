package net.arcation.allegiance;

import net.arcation.allegiance.targets.BlockTarget;
import net.arcation.allegiance.targets.BlockTargetType;
import net.arcation.allegiance.targets.PlaytimeTarget;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mr_Little_Kitty on 3/15/2017.
 */
public class ConfigManager
{
	private final FileConfiguration config;

	public ConfigManager(JavaPlugin plugin)
	{
		this.config = plugin.getConfig();
	}

	public PlaytimeTarget getPlayTimeTarget()
	{
		return null;
	}

	public Map<BlockTargetType,List<BlockTarget>> getBlockTargets()
	{
		return null;
	}
}
