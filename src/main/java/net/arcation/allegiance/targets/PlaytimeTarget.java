package net.arcation.allegiance.targets;

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
	public String getCompletionString(int value)
	{
		return String.format("Played %s/%s minutes. %s complete.",value,minutesRequired,Target.format.format(getPercentCompleted(value)*100));
	}

	public int getMinutesRequired()
	{
		return minutesRequired;
	}
}
