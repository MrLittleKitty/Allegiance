package net.arcation.allegiance.commands;

import net.arcation.allegiance.Allegiance;
import net.arcation.allegiance.Lang;
import net.arcation.allegiance.data.PlayerData;
import net.arcation.allegiance.targets.Target;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Mr_Little_Kitty on 3/1/2017.
 */
public class AllegianceCommand implements CommandExecutor
{
	private final Allegiance allegiance;
	private final boolean detailedInfo;

	public AllegianceCommand(Allegiance allegiance, boolean detailedAllegianceInfo)
	{
		this.allegiance = allegiance;
		this.detailedInfo = detailedAllegianceInfo;
		allegiance.getCommand("Allegiance").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		//Checking the allegiance of the using player
		if(args.length < 1)
			return usedAllegianceCommand(sender);
		else
		{
			if(args[0].equalsIgnoreCase("info"))
			{

			}
			else
			{
				if(!(sender instanceof Player) || sender.isOp())
				{
					Player player = Bukkit.getPlayer(args[0]);
					if(player == null)
					{
						sender.sendMessage("Couldn't find the given player. Either they're offline or you mistyped.");
						return true;
					}

					PlayerData data = allegiance.getPlayer(player.getUniqueId());
					if(data == null)
					{
						allegiance.log("Player ["+player.getName()+"] was not in the player cache but is online.");
						sender.sendMessage("Couldn't find the players allegiance data because of an error. RIP");
						return true;
					}

					if(data.isBypassed())
					{
						sender.sendMessage("Player ["+player.getName()+"] is bypassed.");
						return true;
					}

					if(data.isAllegiant())
						sender.sendMessage("Player ["+player.getName()+"] is allegiant.");
					else
						sender.sendMessage(String.format("Player ["+player.getName()+"] is %s%% allegiant.",data.getRoundedAllegiantPercent()));
					return true;
				}
				else return usedAllegianceCommand(sender);
			}
		}
		return false;
	}

	private boolean usedAllegianceCommand(CommandSender sender)
	{
		if(!(sender instanceof Player))
		{
			sender.sendMessage("I'm just some random guy but my guess is that the console is allegiant, No?");
			return true;
		}

		PlayerData data = allegiance.getPlayer(((Player) sender).getUniqueId());
		if(data == null)
		{
			allegiance.log("Player ["+sender.getName()+"] used /Allegiance but was not in the player cache");
			sender.sendMessage("Hmmm, couldn't find your allegiance data. RIP.");
			return true;
		}

		if(data.isBypassed())
		{
			sender.sendMessage(Lang.UserIsBypassed);
			return true;
		}

		if(data.isAllegiant()) //If they are already allegiant then just print it
			sender.sendMessage(Lang.UserIsAllegiant);
		else
		{
			sender.sendMessage(String.format(Lang.UserNotAllegiant,data.getRoundedAllegiantPercent()));
			if(detailedInfo)
				sender.sendMessage(data.getTargetStrings());
		}

		return true;
	}
}
