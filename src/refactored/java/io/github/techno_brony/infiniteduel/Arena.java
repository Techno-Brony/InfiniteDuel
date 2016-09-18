package io.github.techno_brony.infiniteduel;

import com.sun.istack.internal.Nullable;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Arena {
    private HashMap<Location, Boolean> spawnLocations = new HashMap<>();

    private String author = "";
    private String name = "";
    private boolean isInUse;

    private static HashMap<String, Arena> arenas;

    public Arena(String author, String name) {
        this.author = author;
        this.name = name;
    }

    public void addSpawnLocation(Location location) {
        spawnLocations.put(location, false);
    }

    /**
     * Gets a random spawn location that is not in use
     * @param willBeUsed If true the location returned will be marked as in use
     * @return A location or null if there isnt a location available
     */
    @Nullable
    public Location getUnusedSpawnLocation(boolean willBeUsed) {
        Iterator it = spawnLocations.entrySet().iterator();
        while (it.hasNext()) {
            boolean locationInUse = (Boolean) ((Map.Entry) it.next()).getValue();
            if (!locationInUse) return (Location) ((Map.Entry) it.next()).getKey();
        }
        return null;
    }

    /**
     * Gets a random Arena that is not in use from arenas
     * @param willBeUsed If true the arena returned will be marked as in use
     * @return An arena or null if there isnt an arena available
     * @see Arena#arenas
     */
    @Nullable
    public static Arena getUnusedArena(boolean willBeUsed) {
        Iterator it = arenas.entrySet().iterator();
        while (it.hasNext()) {
            boolean arenaInUse = (Boolean) ((Map.Entry) it.next()).getValue();
            if (!arenaInUse) return (Arena) ((Map.Entry) it.next()).getKey();
        }
        return null;
    }

    /**
     * Sets all the spawnLocations inUse flag to false
     * Sets the arena inUse flag to false
     */
    public void resetArena() {
        for (Location location : spawnLocations.keySet()) {
            spawnLocations.put(location, false);
        }
        isInUse = false;
    }

    /**
     * Adds this Arena instance to the static container of Arenas
     * used in static functions
     * @see Arena#arenas
     */
    public void addSelf() {
        arenas.put(this.getName(), this);
    }

    public String getAuthor() {
        return author;
    }

    public String getName() {
        return name;
    }

}
