package net.arcation.allegiance.util;

import org.bukkit.Material;

/**
 * Created by ewolfe on 3/14/2017.
 */
public class Hash
{
    public static int HashCode(int one, int two, int three)
    {
        int result = one;
        result = 31 * result + two;
        result = 31 * result + three;
        return result;
    }
}
