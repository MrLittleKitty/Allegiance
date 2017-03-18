package net.arcation.allegiance.targets;

/**
 * Created by ewolfe on 3/14/2017.
 */
public enum BlockTargetType
{
    BREAK("Broke"),
    PLACE("Placed");

    private final String pastTense;
    private BlockTargetType(String pastTense)
	{
		this.pastTense = pastTense;
	}

	public String getPastTenseString()
	{
		return pastTense;
	}
}
