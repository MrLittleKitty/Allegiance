package net.arcation.allegiance.listeners;

import net.arcation.allegiance.Allegiance;
import net.arcation.allegiance.ConfigManager;
import net.arcation.allegiance.Lang;
import net.arcation.allegiance.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
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

		CombatListeners combatListener = null;
		int threshold = manager.getAllowPvpPercent();
		if(threshold > 0)
			combatListener = new CombatListeners(allegiance,threshold,manager.getFightTimer(),manager.getTagOnReinforcementDamage());

		if(pluginManager.getPlugin("Citadel") != null)
		{
			if(pluginManager.getPlugin("NameLayer") != null)
			{
				threshold = manager.getAllowReinforcementDamagePercent();
				if (threshold > 0)
					new CitadelListeners(allegiance, threshold, combatListener);
			}
			else allegiance.log("WARN: NameLayer is not present. Some features will not be functional.");
		}
		else allegiance.log("WARN: Citadel is not present. Some features will not be functional.");

		if(pluginManager.getPlugin("Bastion") != null)
		{
			threshold = manager.getAllowBastionDamagePercent();
			if(threshold > 0)
				new BastionListeners(allegiance, threshold);
		}
		else allegiance.log("WARN: Bastion is not present. Some features will not be functional.");

		this.allegiance = allegiance;
		this.allowFirePercent = manager.getAllowFirePercent();

		if(allowFirePercent > 0)
		{
			lavaMessage = String.format(Lang.DontPlaceLava, allowFirePercent);
			fireMessage = String.format(Lang.DontLightShitOnFire, allowFirePercent);

			pluginManager.registerEvents(this, allegiance);
		}
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

		//If they are more than enough allegiant let them do it
		if(data.getRoundedAllegiantPercent() >= allowFirePercent)
			return;

		//Otherwise stop it and send them a message
		event.setCancelled(true);

		allegiance.sendMessageToPlayer(event.getPlayer(),lavaMessage);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onPlayerLightingShitOnFire(BlockIgniteEvent event)
	{
		//We only care about flint and steel
		if(event.getCause() != BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL)
			return;

		//We don't want to stop them if they are lighting a nether portal
		Block theOneTheyreLightingOnFire = event.getBlock().getRelative(BlockFace.DOWN);
		if(theOneTheyreLightingOnFire != null && theOneTheyreLightingOnFire.getType() == Material.OBSIDIAN)
			return;

		//Check if they are bypassed or completely allegiant
		PlayerData data = allegiance.getPlayer(event.getPlayer().getUniqueId());
		if(data.isBypassed() || data.isAllegiant())
			return;

		//If they are more than enough allegiant let them do it
		if(data.getRoundedAllegiantPercent() >= allowFirePercent)
			return;

		//Otherwise stop it and send them a message
		event.setCancelled(true);

		allegiance.sendMessageToPlayer(event.getPlayer(),fireMessage);
	}

}
