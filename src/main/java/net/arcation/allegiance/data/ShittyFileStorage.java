package net.arcation.allegiance.data;

import net.arcation.allegiance.targets.Target;
import net.arcation.allegiance.util.Pair;
import org.bukkit.Bukkit;
import vg.civcraft.mc.citadel.command.commands.Off;

import java.io.*;
import java.util.*;

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
                        if(line.equalsIgnoreCase("bypassed"))
                        {
                            data.setBypassed(true);
                            continue;
                        }

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

                if(data.isBypassed())
                    writer.write("bypassed");

                writer.flush();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public Iterator<Pair<UUID,PlayerData>> getAllStroredDataIterator(List<Target> targets)
    {
        return new OfflineIterator(targets);
    }

    private class OfflineIterator implements Iterator<Pair<UUID,PlayerData>>
    {
        private final List<Target> targets;
        private final File[] files;
        private int index;

        public OfflineIterator(List<Target> targets)
        {
            this.targets = targets;
            files = storageLocation.listFiles();
            index = -1;
        }

        @Override
        public boolean hasNext()
        {
            return index < files.length-1;
        }

        @Override
        public Pair<UUID, PlayerData> next()
        {
            index++;
            if(index >= files.length)
                return null;
            File file = files[index];
            String uuidName = file.getName();
            uuidName = uuidName.substring(0,uuidName.indexOf('.'));
            UUID uuid = UUID.fromString(uuidName);
            PlayerData data = loadPlayerData(uuid,targets);
            return new Pair<UUID,PlayerData>(uuid,data);
        }
    }
}
