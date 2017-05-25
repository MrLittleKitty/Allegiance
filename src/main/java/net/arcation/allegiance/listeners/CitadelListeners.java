package net.arcation.allegiance.listeners;

import net.arcation.allegiance.Allegiance;
import net.arcation.allegiance.Lang;
import net.arcation.allegiance.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import vg.civcraft.mc.citadel.events.ReinforcementDamageEvent;
import vg.civcraft.mc.citadel.reinforcement.PlayerReinforcement;

import java.util.UUID;

/**
 * Created by Mr_Little_Kitty on 3/15/2017.
 */
public class CitadelListeners implements Listener
{
	private final Allegiance allegiance;
	private final CombatListeners combatListeners;
	private final int allowDamagePercent;
	private final String damageString;

	public CitadelListeners(Allegiance allegiance, int allowReinforcementDamage, CombatListeners combatListeners)
	{
		this.allegiance = allegiance;
		this.combatListeners = combatListeners;

		this.allowDamagePercent = allowReinforcementDamage;

		damageString = String.format(Lang.CantDamageReinforcements,allowReinforcementDamage);

		Bukkit.getPluginManager().registerEvents(this,allegiance);
	}

	@EventHandler(priority = EventPriority.LOW,ignoreCancelled = true)
	public void onReinforcementDamage(ReinforcementDamageEvent event)
	{
		//Only care if a player damages a reinforcement (as opposed to say, TNT)
		if(event.getPlayer() == null)
			return;

		UUID id = event.getPlayer().getUniqueId();
		PlayerData data = allegiance.getPlayer(id);

		//If they arent bypassed, arent completely allegiant, and dont meet the percent requirement, stop the event
		if(!data.isBypassed() && !data.isAllegiant() && data.getRoundedAllegiantPercent() < allowDamagePercent)
		{
			//Cancel the event so they dont damage the block and send them a message
			event.setCancelled(true);
			allegiance.sendMessageToPlayer(event.getPlayer(),damageString);
			return;
		}

		//If the combat listener isn't null then tell it that a player has damaged a reinforcement
		if(combatListeners != null && event.getReinforcement() instanceof PlayerReinforcement)
		{
			PlayerReinforcement reinforcement = (PlayerReinforcement)event.getReinforcement();

			//If the player that breaks the block isn't on the group then put them in combat
			if(!reinforcement.getGroup().isCurrentMember(id))
				combatListeners.playerDamagedReinforcement(event.getPlayer(),reinforcement.getGroup().getAllMembers());
		}
	}


}
