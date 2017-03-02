package net.arcation.allegiance;

import net.arcation.allegiance.targets.Target;

import java.util.HashMap;

/**
 * Created by Mr_Little_Kitty on 2/24/2017.
 */
public class PlayerData
{
    private boolean allegiant;
    private boolean bypassed;
    private HashMap<Target,Integer> targets;

    public PlayerData()
    {
        targets = new HashMap<>();
        allegiant = false;
        bypassed = false;
    }

    public boolean isAllegiant()
    {
        return allegiant;
    }

    public boolean isBypassed()
    {
        return bypassed;
    }

    public String[] getTargetStrings()
    {
        return null;
    }

    public double getAllegiantPercent()
    {
        return 0;
    }

    public void addTarget(Target target)
    {
        //this.targets.put(target,data);
        recheckAllegiance();
    }

    public void recheckAllegiance()
    {
//        for(TargetData d : targets.values())
//        {
//            if (!d.isCompleted())
//            {
//                allegiant = false;
//                return;
//            }
//        }
//        allegiant = true;
    }
}
