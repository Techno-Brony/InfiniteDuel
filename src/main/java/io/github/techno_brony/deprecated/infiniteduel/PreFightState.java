package io.github.techno_brony.deprecated.infiniteduel;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class PreFightState {
    private Location location;
    private Double health;
    private int food;
    private float foodSaturation;
    private Inventory inventory;
    private GameMode gameMode;

    PreFightState(Player player) {
        location = player.getLocation();
        health = player.getHealth();
        food = player.getFoodLevel();
        inventory = player.getInventory();
        gameMode = player.getGameMode();
        foodSaturation = player.getSaturation();
    }

    public void updateFromState(Player player) {
        player.teleport(location);
        player.setHealth(health);
        player.setFoodLevel(food);
        player.setSaturation(foodSaturation);
        player.setGameMode(gameMode);
        player.getInventory().clear();
        player.getInventory().setContents(inventory.getContents());
    }
}
