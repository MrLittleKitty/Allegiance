package net.arcation.allegiance.targets;

import org.bukkit.Material;

/**
 * Created by Mr_Little_Kitty on 3/2/2017.
 */
public class BlockTarget extends Target
{
	private final int amount;
	private final Material material;
	private final byte data;

	public BlockTarget(int id, int amount, Material material, byte data)
	{
		super(id);
		this.amount = amount;
		this.material = material;
		this.data = data;
	}

	public BlockTarget(int id, int amount, Material material)
	{
		this(id,amount,material,(byte)-1);
	}

	@Override
	public boolean isCompleted(int score)
	{
		return score >= amount;
	}

	@Override
	public double getPercentCompleted(int score)
	{
		double val = (double)score/(double)amount;
		if(val > 1)
			return 1D;
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
		String builder = "%s " + value + '/' + amount +
				" blocks of type " + (data == -1 ? material.toString() : material.toString() + ":" + data) +
				". " + Target.format.format(getPercentCompleted(value) * 100) +
				"% complete.";
		return builder;
	}

	public Material getMaterial()
	{
		return material;
	}

	public byte getData()
	{
		return data;
	}
}
