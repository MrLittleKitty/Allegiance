package net.arcation.allegiance.listeners;

import net.arcation.allegiance.Allegiance;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

/**
 * Created by Mr_Little_Kitty on 3/15/2017.
 */
public class NaughtyListeners implements Listener
{
	public NaughtyListeners(Allegiance allegiance)
	{
		PluginManager manager = Bukkit.getPluginManager();
		if(manager.getPlugin("Citadel") != null)
			new CitadelListeners(allegiance);

		if(manager.getPlugin("Bastion") != null)
			new BastionListeners(allegiance);

		if(manager.getPlugin("CombatTagPlus") != null)
			new CombatTagListeners(allegiance);

		manager.registerEvents(this,allegiance);
	}


}
