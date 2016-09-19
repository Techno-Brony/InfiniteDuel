package io.github.techno_brony.infiniteduel;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

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
        if (queueType != QueueType.NOT_IN_QUEUE) {
            return;
        }
        kitsWanted.remove(kit);
    }

    public void addWantedKit(Kit kit) {
        if (queueType != QueueType.NOT_IN_QUEUE) {
            return;
        }
        kitsWanted.add(kit);
    }

    public void disableAutoduel() {
        DuelQueue.getInstance().removePlayerFromQueue(player);
        queueType = QueueType.NOT_IN_QUEUE;
    }

    public void enableAutoduel() {
        if (kitsWanted.size() <= 0) { return; }
        DuelQueue.getInstance().addPlayerToQueue(plugin.getServer().getPlayer(player), kitsWanted);
        queueType = QueueType.AUTO_DUEL_ENABLED;
    }

    public void beginManualDuel() {
        if (kitsWanted.size() <= 0) { return; }
        if (playerToDuel != null) {
            sendDuelRequest(playerToDuel, kitsWanted.iterator().next());
        }
        if (!checkIfFightAvailable()) {
            DuelQueue.getInstance().addPlayerToQueue(plugin.getServer().getPlayer(player), kitsWanted);
            queueType = QueueType.MANUAL_DUEL_STARTED;
        }
    }

    public void cancelManualDuel() {
        //TODO dont allow if waiting for arena
        DuelQueue.getInstance().removePlayerFromQueue(player);
        queueType = QueueType.NOT_IN_QUEUE;
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

    private boolean checkIfFightAvailable() {
        for (Kit kit : kitsWanted) {
            if (DuelQueue.getInstance().hasPlayerAvailable(kit)) {
                UUID p = DuelQueue.getInstance().getDuelPlayer(kit, true);
                startFight(plugin.getServer().getPlayer(p), kit);
                return true;
            }
        }
        return false;
    }

    private void startFight(Player opponent, Kit kit) {
        Player own = plugin.getServer().getPlayer(player);
        PlayerState.addPlayerState(own);
        PlayerState.addPlayerState(opponent);
        Arena arena = Arena.getUnusedArena(true);
        if (arena == null) {
            plugin.getLogger().log(Level.SEVERE, "Empty arena missing. Add more arenas!");
            arena = Arena.getAnyArena();
        }
        preparePlayer(arena, own, kit);
        preparePlayer(arena, opponent, kit);
        FightsHandler.addFight(own, opponent);
    }

    private void preparePlayer(Arena arena, Player player, Kit kit) {
        player.teleport(arena.getUnusedSpawnLocation(true));
        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setSaturation(20);
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();
        player.getInventory().setContents(kit.getItems());
        player.closeInventory();
    }

    private void sendDuelRequest(UUID opponent, Kit kit) {
        Player p = plugin.getServer().getPlayer(opponent);
        if (p == null) {
            return;
        }
        IChatBaseComponent jsonText = IChatBaseComponent.ChatSerializer.a("[{\"text\":\"" + p.getName() + " \",\"color\":\"dark_aqua\"},{\"text\":\"sent you a duel request!\",\"color\":\"gold\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"\"}},{\"text\":\" Click me to accept\",\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/acceptduel\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"Kit: " + kit.getName() + "\"}]}}},{\"text\":\" or \",\"color\":\"gold\"},{\"text\":\"click me to decline.\",\"color\":\"red\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/declineduel\"}}]");

        PacketPlayOutChat packet = new PacketPlayOutChat(jsonText);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
    }

}

enum QueueType {
    AUTO_DUEL_ENABLED,
    NOT_IN_QUEUE,
    MANUAL_DUEL_STARTED
}