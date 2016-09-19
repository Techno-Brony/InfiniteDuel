package io.github.techno_brony.infiniteduel;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class DuelQueue {
    private HashMap<UUID, Set<Kit>> duelQueue = new HashMap<>();

    private static DuelQueue instance = new DuelQueue();

    public static DuelQueue getInstance() {
        return instance;
    }

    public void removePlayerFromQueue(UUID player) {
        duelQueue.remove(player);
    }

    public void addPlayerToQueue(Player player, Set<Kit> kits) {
        duelQueue.put(player.getUniqueId(), kits);
    }

    /**
     * Checks if there is a player than can accept the kit
     * @param kit The kit to be checked for
     * @return True if there is a player otherwise false
     */
    public boolean hasPlayerAvailable(Kit kit) {
        for (UUID uuid : duelQueue.keySet()) {
            if (duelQueue.get(uuid).contains(kit)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets a player that can accept kit
     * @param kit The kit that is to be checked for
     * @param removeFromQueue If the player returned should be removed from the queue
     * @return The uuid that can accept the kit or if none is found returns null
     */
    public UUID getDuelPlayer(Kit kit, boolean removeFromQueue) {
        for (UUID uuid : duelQueue.keySet()) {
            if (duelQueue.get(uuid).contains(kit)) {
                if (removeFromQueue) {
                    removePlayerFromQueue(uuid);
                }
                return uuid;
            }
        }
        return null;
    }

}
