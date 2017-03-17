package net.arcation.allegiance.listeners;

import net.arcation.allegiance.Allegiance;
import net.arcation.allegiance.ConfigManager;
import net.arcation.allegiance.Lang;
import net.arcation.allegiance.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.plugin.PluginManager;

/**
 * Created by Mr_Little_Kitty on 3/15/2017.
 */
public class NaughtyListeners implements Listener
{
	private final Allegiance allegiance;
	private int allowFirePercent;
	private String lavaMessage;
	private String fireMessage;

	public NaughtyListeners(Allegiance allegiance, ConfigManager manager)
	{
		PluginManager pluginManager = Bukkit.getPluginManager();

		if(pluginManager.getPlugin("Citadel") != null)
			new CitadelListeners(allegiance,manager.getAllowInventoryBreakingPercent());

		if(pluginManager.getPlugin("Bastion") != null)
			new BastionListeners(allegiance,manager.getAllowBastionDamagePercent());

		if(pluginManager.getPlugin("CombatTagPlus") != null)
			new CombatTagListeners(allegiance,manager.getAllowPvpPercent());

		this.allegiance = allegiance;
		this.allowFirePercent = manager.getAllowFirePercent();

		lavaMessage = String.format(Lang.DontPlaceLava,allowFirePercent);
		fireMessage = String.format(Lang.DontLightShitOnFire,allowFirePercent);

		pluginManager.registerEvents(this,allegiance);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void lavaPlaceEvent(PlayerBucketEmptyEvent event)
	{
		//We only care about them placing lava
		if (event.getBucket() != Material.LAVA_BUCKET)
			return;

		//Check if they are bypassed or completely allegiant
		PlayerData data = allegiance.getPlayer(event.getPlayer().getUniqueId());
		if(data.isBypassed() || data.isAllegiant())
			return;

		if(data.getRoundedAllegiantPercent() >= allowFirePercent)
			return;

		event.setCancelled(true);

		allegiance.sendMessageToPlayer(event.getPlayer(),lavaMessage);
	}
}
