package io.github.techno_brony.infiniteduel;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;

public class LookForArena extends BukkitRunnable {

    private final Main plugin;
    private final Player player;
    private final Map.Entry tempEntry;
    private final String s;

    public LookForArena(Main plugin, Player player, Map.Entry tempEntry, String s) {
        this.plugin = plugin;
        this.player = player;
        this.tempEntry = tempEntry;
        this.s = s;
    }

    public void run() {
        for (Arena arena : plugin.arenas) {
            if (!arena.isInUse()) {
                plugin.lookingForArena.remove(player.getUniqueId());
                plugin.listener.startFight(player, plugin.getServer().getPlayer((UUID) tempEntry.getKey()),
                        plugin.kits.get(s), arena);
                this.cancel();
                return;
            }
        }
    }

}
