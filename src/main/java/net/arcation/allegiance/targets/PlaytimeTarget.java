package net.arcation.allegiance.targets;

import net.arcation.allegiance.AllegianceInfo;
import org.bukkit.ChatColor;

/**
 * Created by Mr_Little_Kitty on 3/2/2017.
 */
public class PlaytimeTarget extends Target
{
	private final int minutesRequired;

	public PlaytimeTarget(int id, int minutesRequired)
	{
		super(id);
		this.minutesRequired = minutesRequired;
	}

	@Override
	public boolean isCompleted(int score)
	{
		return score >= minutesRequired;
	}

	@Override
	public double getPercentCompleted(int score)
	{
		double val = (double)score/(double)minutesRequired;
		if(val > 1)
			return 1;
		return val;
	}

	@Override
	public int increase(int value, int increaseValue)
	{
		return value + increaseValue;
	}

	@Override
	public String getCompletionString(int value, AllegianceInfo info)
	{
	    if(info == AllegianceInfo.DETAILED)
		    return String.format(ChatColor.GREEN+"Played "+ ChatColor.WHITE+"%s/%s"+ChatColor.GREEN +" minutes. "+ChatColor.WHITE+"%s%%"+ChatColor.GREEN +" complete.",value,minutesRequired,Target.format.format(getPercentCompleted(value)*100));
	    return String.format(ChatColor.GREEN+"Minutes played: %s",(value > minutesRequired ? ChatColor.GREEN+("Completed") : ChatColor.RED+("Incomplete")) );
	}

	public int getMinutesRequired()
	{
		return minutesRequired;
	}
}
