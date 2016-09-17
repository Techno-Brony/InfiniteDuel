package io.github.techno_brony.infiniteduel;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;

public class Arena {
    private boolean isInUse = false;

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    private ArrayList<Location> spawnLocations = new ArrayList<>();
    private String author = "";
    private String name = "";
    private World world;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isInUse() {
        return isInUse;
    }

    public void setInUse(boolean inUse) {
        isInUse = inUse;
    }

    public ArrayList<Location> getSpawnLocations() {
        return spawnLocations;
    }

    public void setSpawnLocations(ArrayList<Location> spawnLocations) {
        this.spawnLocations = spawnLocations;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
