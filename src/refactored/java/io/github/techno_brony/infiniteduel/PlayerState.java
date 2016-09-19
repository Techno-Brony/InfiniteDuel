package io.github.techno_brony.infiniteduel;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.UUID;

public class PlayerState {
    private static HashMap<UUID, PlayerState> playerStates = new HashMap<>();

    private Location location;
    private Double health;
    private int food;
    private float foodSaturation;
    private Inventory inventory;
    private GameMode gameMode;

    private PlayerState(Player player) {
        location = player.getLocation();
        health = player.getHealth();
        food = player.getFoodLevel();
        inventory = player.getInventory();
        gameMode = player.getGameMode();
        foodSaturation = player.getSaturation();
    }

    private void updateFromState(Player player) {
        player.teleport(location);
        player.setHealth(health);
        player.setFoodLevel(food);
        player.setSaturation(foodSaturation);
        player.setGameMode(gameMode);
        player.getInventory().clear();
        player.getInventory().setContents(inventory.getContents());
    }

    /**
     * Configures the player to a previous state
     * @param player The player to be configured
     * @param delete Whether the playerstate should be removed afterwards
     */
    public static void loadPlayerState(Player player, boolean delete) {
        playerStates.get(player.getUniqueId()).updateFromState(player);
        if (delete) {
            deletePlayerState(player);
        }
    }

    public static void deletePlayerState(Player player) {
        playerStates.remove(player.getUniqueId());
    }

    public static void addPlayerState(Player player) {
        playerStates.put(player.getUniqueId(), new PlayerState(player));
    }

}
