package net.arcation.allegiance.listeners;

import net.arcation.allegiance.Allegiance;
import net.arcation.allegiance.Lang;
import net.arcation.allegiance.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;

import java.util.*;

/**
 * Created by Mr_Little_Kitty on 3/15/2017.
 */
public class CombatListeners implements Listener, Runnable
{
	private final Allegiance allegiance;
	private static final int CLEANUP_FIGHTING_TIMER = 10; //Every 10 minutes

	private final int allowPvpPercent;
	private final long fightTimer;
	private final String cantPVPMessage;
	private final boolean tagOnReinforcementDamage;

	private final Map<UUID,HashMap<UUID,Long>> playersThatCanFight;

	public CombatListeners(Allegiance allegiance, int allowPvpPercent, int fightTimer, boolean tagOnReinforcementDamage)
	{
		this.allegiance = allegiance;

		this.allowPvpPercent = allowPvpPercent;
		this.fightTimer = fightTimer*1000; //Convert the parameter (in seconds) to milliseconds
		cantPVPMessage = String.format(Lang.CantPvp,allowPvpPercent);
		this.tagOnReinforcementDamage = tagOnReinforcementDamage;

		playersThatCanFight = new HashMap<>();

		Bukkit.getPluginManager().registerEvents(this, allegiance);

		//TODO--change the initial delay higher than a minute
		Bukkit.getScheduler().runTaskTimer(allegiance,this,20*60*1,20*60*CLEANUP_FIGHTING_TIMER);
	}

	@EventHandler(priority = EventPriority.LOW,ignoreCancelled = true)
	public void onPlayerAttackAnotherPlayer(EntityDamageByEntityEvent event)
	{
		if(event.getDamager() instanceof Player && event.getEntity() instanceof Player)
		{
			Player attacker = (Player)event.getDamager();
			Player target = (Player)event.getEntity();

			event.setCancelled(handleAttack(attacker,target));
		}
		else if(event.getDamager() instanceof Projectile && event.getEntity() instanceof Player)
		{
			Projectile projectile = (Projectile)event.getDamager();
			if(projectile.getShooter() instanceof Player)
				event.setCancelled(handleAttack((Player)projectile.getShooter(),(Player)event.getEntity()));
		}
	}

	@EventHandler(priority = EventPriority.LOW,ignoreCancelled = true)
	public void onSomeoneThrowAPotion(PotionSplashEvent event)
	{
		if(event.getEntity().getShooter() instanceof Player)
		{
			Player attacker = (Player)event.getEntity().getShooter();

			Iterator<LivingEntity> itt = event.getAffectedEntities().iterator();
			while(itt.hasNext())
			{
				LivingEntity entity = itt.next();
				if(entity instanceof Player)
				{
					if(handleAttack(attacker, (Player) entity))
					{
						event.setCancelled(true);
						return;
					}
				}
			}
		}
	}

	//Return true if we are going to STOP THE DAMAGE
	//Return false if we are gooing to LET THE DAMAGE HAPPEN
	private boolean handleAttack(Player attacker, Player target)
	{
		//If the player is attacking themselves then just let the idiot do it
		if(attacker.getName().equals(target.getName()))
			return false;

		PlayerData attackerData = allegiance.getPlayer(attacker.getUniqueId());
		PlayerData targetData = allegiance.getPlayer(target.getUniqueId());

		//If the attacker is allegiant
		if(attackerData.isBypassed()
				|| attackerData.isAllegiant()
				|| attackerData.getRoundedAllegiantPercent() >= allowPvpPercent)
		{
			if(!targetData.isBypassed() && !targetData.isAllegiant() && targetData.getRoundedAllegiantPercent() < allowPvpPercent)
				playerCanFight(target.getUniqueId(),attacker.getUniqueId()); //Make the target able to fight back

			//Let the damage happen if the attacker passed the allegiance check
			return false;
		}
		else //The attacker isn't dedicated
		{
			if(canPlayerFight(attacker.getUniqueId(),target.getUniqueId()))
			{
				//First make sure to extend the attack timer so they can keep being able to fight
				playerCanFight(attacker.getUniqueId(),target.getUniqueId());

				//We are going to let the damage happen
				return false;
			}
			else //If the attacker can't fight the victim then we are going to stop the damage
			{
				//Tell them that they aren't allowed to pvp until they reach the allegiance amount
				allegiance.sendMessageToPlayer(attacker,cantPVPMessage);
				return true;
			}
		}
	}

	//private final Map<UUID,HashMap<UUID,Long>> playersThatCanFight;

	private void playerCanFight(UUID player, UUID target)
	{
		HashMap<UUID,Long> map = playersThatCanFight.computeIfAbsent(player, k -> new HashMap<>());
		map.put(target,System.currentTimeMillis()+fightTimer);
	}

	private boolean canPlayerFight(UUID attacker, UUID target)
	{
		//Get the map for the players that the attacker is allowed to fight
		HashMap<UUID,Long> playersTheyCanFight = playersThatCanFight.get(attacker);
		if(playersTheyCanFight == null) //If there is no map then they cant fight anyone so return false
			return false;

		//If the map doesn't have an entry for the target, then return false cause they cant fight him
		if(!playersTheyCanFight.containsKey(target))
			return false;

		//Otherwise they are allowed to fight him if the current time is less than the one in the map
		//AKA--The fight timer hasn't expired yet
		return System.currentTimeMillis() <= playersTheyCanFight.get(target);
	}

	//The passed in player has passed the allegiance check for damaging citadel reinforcements
	void playerDamagedReinforcement(Player player, List<UUID> groupMembers)
	{
		//Only do any work if breaking a citadel reinforcement cause group members to be able to fight that player
		if(tagOnReinforcementDamage)
		{
			for (UUID id : groupMembers)
			{
				Player target = Bukkit.getPlayer(id);
				if (target != null) //This means that the player is online
				{
					PlayerData data = allegiance.getPlayer(id);
					//If this player can not already fight, they can now fight the person that broke their block
					if(!data.isBypassed() && !data.isAllegiant() && data.getRoundedAllegiantPercent() < allowPvpPercent)
						playerCanFight(id,player.getUniqueId());
				}
			}
		}
	}

	@Override
	public void run()
	{
		allegiance.log("-Running an update to clean up the fight timers");

		//This will be run every so often to clean up fighting players list
		Iterator<Map.Entry<UUID,HashMap<UUID,Long>>> topIterator = playersThatCanFight.entrySet().iterator();
		while(topIterator.hasNext())
		{
			Map.Entry<UUID,HashMap<UUID,Long>> entry = topIterator.next();
			Iterator<Map.Entry<UUID,Long>> fightIterator = entry.getValue().entrySet().iterator();
			while(fightIterator.hasNext())
			{
				Map.Entry<UUID,Long> fightTimer = fightIterator.next();
				if(System.currentTimeMillis() > fightTimer.getValue())
					fightIterator.remove();
			}

			//If we cleared out all the expired timers then remove this whole entry
			if(entry.getValue().isEmpty())
				topIterator.remove();
		}
	}
}
