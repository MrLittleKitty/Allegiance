package net.arcation.allegiance.targets;

import net.arcation.allegiance.AllegianceInfo;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

/**
 * Created by Mr_Little_Kitty on 2/24/2017.
 */
public abstract class Target
{
    public static final DecimalFormat format = new DecimalFormat("0.00");

    private int id;
    public Target(int id)
    {
        this.id = id;
    }

    public abstract boolean isCompleted(int score);

    public abstract double getPercentCompleted(int score);

    public abstract int increase(int value, int increaseValue);

    public abstract String getCompletionString(int value, AllegianceInfo info);

    public int getId()
    {
        return id;
    }

    @Override
    public int hashCode()
    {
        return id;
    }

    @Override
    public boolean equals(Object other)
    {
        if(other instanceof Target)
            return id == ((Target)other).id;
        return false;
    }
}
