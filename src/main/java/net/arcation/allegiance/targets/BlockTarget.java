package net.arcation.allegiance.targets;

import net.arcation.allegiance.AllegianceInfo;
import org.bukkit.ChatColor;
import org.bukkit.Material;

/**
 * Created by Mr_Little_Kitty on 3/2/2017.
 */
public class BlockTarget extends Target
{
	private final BlockTargetType type;
	private final int amount;
	private final Material material;
	private final byte data;

	public BlockTarget(int id, BlockTargetType type, int amount, Material material, byte data)
	{
		super(id);
		this.type = type;
		this.amount = amount;
		this.material = material;
		this.data = data;
	}

	public BlockTarget(int id, BlockTargetType type, int amount, Material material)
	{
		this(id,type, amount,material,(byte)-1);
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
	public String getCompletionString(int value, AllegianceInfo info)
	{
        String materialString = material == Material.AIR ? "any block" : (data == -1 ? material.toString() : material.toString() + ":" + data);
	    if(info == AllegianceInfo.DETAILED)
        {
            String builder = ChatColor.GREEN + String.format("%s ", type.getPastTenseString()) + ChatColor.WHITE + value + "/" + amount + ChatColor.GREEN + " blocks of type " + materialString + ". " + ChatColor.WHITE + Target.format.format(getPercentCompleted(value) * 100) + "%" + ChatColor.GREEN + " complete.";
            return builder;
        }
        return ChatColor.GREEN + String.format("%s ", type.getPastTenseString()) + "blocks of type " + materialString + ": "+((value > amount ? ChatColor.GREEN+("Completed") : ChatColor.RED+("Incomplete")));
	}

	public Material getMaterial()
	{
		return material;
	}

	public byte getData()
	{
		return data;
	}

	public int getAmount()
	{
		return amount;
	}
}
