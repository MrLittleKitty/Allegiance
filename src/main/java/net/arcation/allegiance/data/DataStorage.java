package net.arcation.allegiance.data;

import net.arcation.allegiance.targets.Target;
import net.arcation.allegiance.util.Pair;

import java.util.*;

/**
 * Created by Mr_Little_Kitty on 3/2/2017.
 */
public interface DataStorage
{
	PlayerData loadPlayerData(UUID id, List<Target> targets);

	void savePlayerData(UUID id, PlayerData data);

	Iterator<Pair<UUID,PlayerData>> getAllStroredDataIterator(List<Target> targets);
}
