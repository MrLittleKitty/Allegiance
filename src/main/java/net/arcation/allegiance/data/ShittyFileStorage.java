package net.arcation.allegiance.data;

import net.arcation.allegiance.targets.Target;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by ewolfe on 3/16/2017.
 */
public class ShittyFileStorage implements DataStorage
{
    private static final String FILE_EXTENSION = ".json";
    private final File storageLocation;

    public ShittyFileStorage(File storageLocation)
    {
        this.storageLocation = storageLocation;
    }

    @Override
    public PlayerData loadPlayerData(UUID id, List<Target> targets)
    {
        PlayerData data = new PlayerData();

        File file = new File(storageLocation,id.toString()+FILE_EXTENSION);
        HashMap<Integer,Integer> fromFile = new HashMap<>();

        if(file.exists())
        {
            try
            {
                String line = null;
                try (BufferedReader reader = new BufferedReader(new FileReader(file)))
                {
                    while ((line = reader.readLine()) != null)
                    {
                        String[] split = line.split(":");
                        fromFile.put(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
                    }
                }
            } catch (FileNotFoundException e)
            {
                e.printStackTrace();
            } catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }

        for(Target target : targets)
        {
            if(fromFile.containsKey(target.getId()))
                data.addTarget(target,fromFile.get(target.getId()));
            else
                data.addTarget(target);
        }

        data.recheckAllegiance();

        return data;
    }

    @Override
    public void savePlayerData(UUID id, PlayerData data)
    {
        try
        {
            File file = new File(storageLocation,id.toString()+FILE_EXTENSION);
            try(FileWriter writer = new FileWriter(file))
            {
                for (Map.Entry<Target, Integer> entry : data.getTargetData().entrySet())
                    writer.write(entry.getKey().getId() + ":" + entry.getValue() + System.lineSeparator());
                writer.flush();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
