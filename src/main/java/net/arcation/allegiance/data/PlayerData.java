package net.arcation.allegiance.data;

import net.arcation.allegiance.targets.Target;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mr_Little_Kitty on 2/24/2017.
 */
public class PlayerData
{
    private boolean allegiant;
    private boolean bypassed;
	private HashMap<Target,Integer> targets;
    private HashMap<String,Object> data;

    public PlayerData()
    {
        targets = new HashMap<>();
        data = new HashMap<>();
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

    public void setBypassed(boolean bypassed)
    {
        this.bypassed = bypassed;
    }

    public String[] getTargetStrings()
    {
        String[] str = new String[targets.size()];
        int i = 0;
        for(Map.Entry<Target,Integer> entry : targets.entrySet())
        {
            str[i] = entry.getKey().getCompletionString(entry.getValue());
            i++;
        }
        return str;
    }

    public Object getData(String key)
    {
        return data.get(key);
    }

    public void setData(String key, Object data)
    {
        this.data.put(key,data);
    }

    public double getAllegiantPercent()
    {
        //Calculate evenly weighted average of the percent complete for each target
        double total = 0;
        for(Map.Entry<Target,Integer> entry : targets.entrySet())
            total += entry.getKey().getPercentCompleted(entry.getValue());
        return total/targets.size();
    }

    public int getRoundedAllegiantPercent()
	{
		return (int)(getAllegiantPercent()*100);
	}

    public void increaseTarget(Target target, int value)
    {
        Integer current = targets.get(target);
        if(current != null)
		{
			int newValue = target.increase(current, value);
			targets.put(target, newValue);

			//If the score before increment didn't meet allegiance but the new one does, then recheck all allegiances
			if(!target.isCompleted(value) && target.isCompleted(newValue))
				recheckAllegiance();
		}
        else
		{
			targets.put(target, value); //Initial value is zero so zero + vale = value

			//Check if maybe it was initialized at being already completed
			if(target.isCompleted(value))
				recheckAllegiance();
		}
    }

    public void addTarget(Target target)
    {
       addTarget(target,0);
    }

    public void addTarget(Target target, int initialValue)
    {
        this.targets.put(target,initialValue);

        //If the initial value doesn't meet the requirement they automatically arent allegiant
        if(!target.isCompleted(initialValue))
            allegiant = false;
    }

    public void recheckAllegiance()
    {
        for(Map.Entry<Target,Integer> entry : targets.entrySet())
        {
            if (!entry.getKey().isCompleted(entry.getValue()))
            {
                allegiant = false;
                return;
            }
        }
        allegiant = true;
    }

    Map<Target,Integer> getTargetData()
	{
		return targets;
	}
}
