package io.github.techno_brony.infiniteduel;

import io.github.techno_brony.infiniteduel.commands.DuelCommand;
import io.github.techno_brony.infiniteduel.commands.InfiniteDuelCommand;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        createAndLoadConfig();
        parseConfigs();
        registerCommands();
    }

    private void createAndLoadConfig() {
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            File file = new File(getDataFolder(), "config.yml");
            if (!file.exists()) {
                getLogger().info("Creating config.yml ...");
                saveDefaultConfig();
            } else {
                getLogger().info("Loading config.yml ...");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerCommands() {
        getCommand(InfiniteDuelCommand.getCommandName()).setExecutor(new InfiniteDuelCommand());
        getCommand(DuelCommand.getCommandName()).setExecutor(new DuelCommand(this));
    }

    private void parseConfigs() {
        parseArenaConfig();
        parseKitConfig();
    }

    private void parseArenaConfig() {
        ConfigurationSection configArenas = getConfig().getConfigurationSection("arenas");
        for (String arenaIndex : configArenas.getKeys(false)) {
            String arenaName = configArenas.getString(arenaIndex + ".name");
            String arenaAuthor = configArenas.getString(arenaIndex + ".author");
            Arena arena = new Arena(arenaAuthor, arenaName);
            World world = getServer().getWorld(configArenas.getString(arenaIndex + ".world"));
            List<String> rawLocations = configArenas.getStringList(arenaIndex + ".spawnLocations");
            for (String rawLocation : rawLocations) {
                String[] rawXYZ = rawLocation.split(" ");
                Double[] XYZ = new Double[3];
                for (int i = 0; i < 3; i++) {
                    XYZ[i] = Double.parseDouble(rawXYZ[i]);
                }
                Location location = new Location(world, XYZ[0], XYZ[1], XYZ[2]);
                arena.addSpawnLocation(location);
            }
            arena.addSelf();
        }
        getLogger().log(Level.INFO, "Arenas Loaded.");
    }

    private void parseKitConfig() {
        ConfigurationSection configKits = getConfig().getConfigurationSection("kits");
        for (String kitIndex : configKits.getKeys(false)) {
            String name = configKits.getString(kitIndex + ".name");
            List<String> rawItemStacks = configKits.getStringList(kitIndex + ".items");
            ArrayList<ItemStack> listItemStacks = new ArrayList<>();
            for (String rawItemStack : rawItemStacks) {
                String[] itemStackData = rawItemStack.split(" ");
                Material itemStackMaterial = Material.matchMaterial(itemStackData[0]);
                Integer numberOfItems = Integer.parseInt(itemStackData[1]);
                listItemStacks.add(new ItemStack(itemStackMaterial, numberOfItems));
            }
            Kit.addKit(new Kit(name, listItemStacks.toArray(new ItemStack[listItemStacks.size()])));
        }
        getLogger().log(Level.INFO, "Kits loaded.");
    }

}
