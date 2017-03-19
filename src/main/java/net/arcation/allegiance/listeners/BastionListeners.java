package net.arcation.allegiance.listeners;

import isaac.bastion.event.BastionDamageEvent;
import net.arcation.allegiance.Allegiance;
import net.arcation.allegiance.Lang;
import net.arcation.allegiance.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * Created by Mr_Little_Kitty on 3/15/2017.
 */
public class BastionListeners implements Listener
{
	private Allegiance allegiance;
	private int percentToDamage;
	private String bastionMessage;

	public BastionListeners(Allegiance allegiance, int percentToDamage)
	{
		this.allegiance = allegiance;
		this.percentToDamage = percentToDamage;

		bastionMessage = String.format(Lang.DontDamageBastions,percentToDamage);

		Bukkit.getPluginManager().registerEvents(this,allegiance);
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onPlayerDamageBastion(BastionDamageEvent event)
	{
		PlayerData data = allegiance.getPlayer(event.getPlayer().getUniqueId());

		//If they are allegiant or bypassed they can damage bastions
		if(data.isBypassed() || data.isAllegiant())
			return;

		//If they are past the threshold then they can do damage
		if(data.getRoundedAllegiantPercent() >= percentToDamage)
			return;

		//Otherwise they cant damage bastions
		event.setCancelled(true);

		allegiance.sendMessageToPlayer(event.getPlayer(), bastionMessage);
	}
}
