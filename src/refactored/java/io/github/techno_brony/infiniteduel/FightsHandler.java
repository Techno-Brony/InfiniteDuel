package io.github.techno_brony.infiniteduel;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class FightsHandler {
    private static HashMap<UUID, UUID> currentFights = new HashMap<>();

    public static void addFight(Player one, Player two) {
        UUID idOne = one.getUniqueId();
        UUID idTwo = two.getUniqueId();
        currentFights.put(idOne, idTwo);
        currentFights.put(idTwo, idOne);
    }
    public static UUID getOtherPlayer(Player player) {
        return currentFights.get(player.getUniqueId());
    }
    public static void removeFight(Player player) {
        UUID two = currentFights.get(player.getUniqueId());
        if (two != null) {
            currentFights.remove(player.getUniqueId());
            currentFights.remove(two);
        }
    }
    public static boolean isInFight(Player player) {
        return currentFights.containsKey(player.getUniqueId());
    }
}
