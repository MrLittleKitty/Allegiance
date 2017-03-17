package net.arcation.allegiance.listeners;

import net.arcation.allegiance.Allegiance;
import net.arcation.allegiance.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

/**
 * Created by Mr_Little_Kitty on 3/15/2017.
 */
public class NaughtyListeners implements Listener
{
	public NaughtyListeners(Allegiance allegiance, ConfigManager manager)
	{
		PluginManager pluginManager = Bukkit.getPluginManager();
		if(pluginManager.getPlugin("Citadel") != null)
			new CitadelListeners(allegiance);

		if(pluginManager.getPlugin("Bastion") != null)
			new BastionListeners(allegiance);

		if(pluginManager.getPlugin("CombatTagPlus") != null)
			new CombatTagListeners(allegiance);

		pluginManager.registerEvents(this,allegiance);
	}


}
