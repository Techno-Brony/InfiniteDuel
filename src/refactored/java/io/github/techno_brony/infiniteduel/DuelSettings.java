package io.github.techno_brony.infiniteduel;

import org.bukkit.entity.Player;

import java.util.*;

public class DuelSettings {
    private final Main plugin;

    private QueueType queueType = QueueType.NOT_IN_QUEUE;
    private UUID playerToDuel = null;

    private Set<Kit> kitsWanted = new HashSet<>();
    private UUID player;

    private static HashMap<UUID, DuelSettings> duelSettings = new HashMap<>();

    public DuelSettings(Main plugin, UUID player) {
        this.plugin = plugin;
        this.player = player;
    }

    public static DuelSettings getDuelSetting(Player p, Main plugin) {
        if (!duelSettings.containsKey(p.getUniqueId())) {
            duelSettings.put(p.getUniqueId(), new DuelSettings(plugin, p.getUniqueId()));
        }
        return duelSettings.get(p.getUniqueId());
    }

    public QueueType getQueueType() {
        return queueType;
    }

    public boolean wantsKit(Kit kit) {
        return kitsWanted.contains(kit);
    }

    public void removeWantedKit(Kit kit) {
        kitsWanted.remove(kit);
    }

    public void addWantedKit(Kit kit) {
        kitsWanted.add(kit);
    }

    public void disableAutoduel() {
        //TODO
    }

    public void enableAutoduel() {
        //TODO
    }

    public void beginManualDuel() {
        //TODO
    }

    public void cancelManualDuel() {
        //TODO
    }

    public UUID getPlayerToDuel() {
        return playerToDuel;
    }

    public void setPlayerToDuel(UUID playerToDuel) {
        this.playerToDuel = playerToDuel;
    }

    public static void destroyDuelSetting(Player p) {
        duelSettings.remove(p.getUniqueId());
    }

}

enum QueueType {
    AUTO_DUEL_ENABLED,
    NOT_IN_QUEUE,
    MANUAL_DUEL_STARTED
}