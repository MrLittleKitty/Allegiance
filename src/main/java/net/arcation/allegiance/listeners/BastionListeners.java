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
	public BastionListeners(Allegiance allegiance)
	{
		this.allegiance = allegiance;
		Bukkit.getPluginManager().registerEvents(this,allegiance);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerDamageBastion(BastionDamageEvent event)
	{
		PlayerData data = allegiance.getPlayer(event.getPlayer().getUniqueId());

		//If they are allegiant or bypassed they can damage bastions
		if(data.isBypassed() || data.isAllegiant())
			return;

		//Otherwise they cant damage bastions
		event.setCancelled(true);

		allegiance.sendMessageToPlayer(event.getPlayer(), Lang.DONTDAMAGEBASTIONS);
	}
}
