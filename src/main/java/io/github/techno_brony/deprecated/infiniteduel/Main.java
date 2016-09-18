package io.github.techno_brony.deprecated.infiniteduel;

import io.github.techno_brony.deprecated.infiniteduel.commands.DuelCommand;
import io.github.techno_brony.deprecated.infiniteduel.commands.InfiniteDuelCommand;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class Main extends JavaPlugin {

    public ArrayList<Arena> arenas = new ArrayList<>();
    public HashMap<UUID, ArrayList<String>> queue = new HashMap<>();
    public ArrayList<UUID> inFightAutoDuel = new ArrayList<>();
    public HashMap<String, Kit> kits = new HashMap<>();
    public HashMap<UUID, PreFightState> preFightState = new HashMap<>();
    public HashMap<UUID, UUID> currentFights = new HashMap<>();
    public ArrayList<UUID> lookingForArena = new ArrayList<>();
    public HashMap<UUID, Arena> playerInArena = new HashMap<>();

    public HashMap<UUID, DuelSettingsSelector> settings = new HashMap<>();
    public EventListener listener;

    @Override
    public void onEnable() {
//        createAndLoadConfig();
//        parseConfig();

        listener = new EventListener(this);
        getServer().getPluginManager().registerEvents(listener, this);

        getCommand("duel").setExecutor(new DuelCommand(this));
        getCommand("infiniteduel").setExecutor(new InfiniteDuelCommand(this));
    }

    @Override
    public void onDisable() {

    }

//    private void createAndLoadConfig() {
//        try {
//            if (!getDataFolder().exists()) {
//                getDataFolder().mkdirs();
//            }
//            File file = new File(getDataFolder(), "config.yml");
//            if (!file.exists()) {
//                getLogger().info("Creating config.yml ...");
//                saveDefaultConfig();
//            } else {
//                getLogger().info("Loading config.yml ...");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
//
//    }

    private void parseConfig() {
//        ConfigurationSection configArenas = getConfig().getConfigurationSection("arenas");
//        for (String arena : configArenas.getKeys(false)) {
//            Arena temp = new Arena();
//            temp.setAuthor(configArenas.getString(arena + ".author"));
//            temp.setName(configArenas.getString(arena + ".name"));
//            temp.setWorld(getServer().getWorld(configArenas.getString(arena + ".world")));
//            List<String> rawLocations = configArenas.getStringList(arena + ".spawnLocations");
//            ArrayList<Location> locations = new ArrayList<>();
//            for (String rawLocation : rawLocations) {
//                String[] rxyz = rawLocation.split(" ");
//                Integer[] xyz = new Integer[3];
//                for (int i = 0; i < rxyz.length; i++) {
//                    xyz[i] = Integer.valueOf(rxyz[i]);
//                }
//                Location tempLocation = new Location(temp.getWorld(), xyz[0], xyz[1], xyz[2]);
//                locations.add(tempLocation);
//            }
//            temp.setSpawnLocations(locations);
//            arenas.add(temp);
//        }
//        getLogger().log(Level.INFO, "Added " + arenas.size() + " arenas.");

//        ConfigurationSection configKits = getConfig().getConfigurationSection("kits");
//        for (String kit : configKits.getKeys(false)) {
//            Kit temp = new Kit();
//            temp.setName(configKits.getString(kit + ".name"));
//            List<String> rawItems = configKits.getStringList(kit + ".items");
//            ArrayList<ItemStack> itemStacks = new ArrayList<>();
//            for (String rawItem : rawItems) {
//                String[] rawItemStack = rawItem.split(" ");
//                Material material = Material.matchMaterial(rawItemStack[0]);
//                Integer amount = Integer.valueOf(rawItemStack[1]);
//                ItemStack itemStack = new ItemStack(material, amount);
//                itemStacks.add(itemStack);
//            }
//            temp.setItems(itemStacks);
//            kits.put(temp.getName(), temp);
//        }
//        getLogger().log(Level.INFO, "Added " + kits.size() + " kits.");
    }
}
