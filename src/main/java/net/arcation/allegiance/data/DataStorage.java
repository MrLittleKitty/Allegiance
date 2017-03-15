package net.arcation.allegiance.data;

import net.arcation.allegiance.targets.Target;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Mr_Little_Kitty on 3/2/2017.
 */
public interface DataStorage
{
	PlayerData loadPlayerData(UUID id, List<Target> targets);

	void savePlayerData(UUID id, PlayerData data);
}
