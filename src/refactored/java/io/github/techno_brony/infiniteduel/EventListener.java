package io.github.techno_brony.infiniteduel;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {

    private final Main plugin;

    public EventListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        DuelSettingsGUIHandler.destroyGUI(event.getPlayer());
        DuelSettings.destroyDuelSetting(event.getPlayer());
        DuelQueue.getInstance().removePlayerFromQueue(event.getPlayer().getUniqueId());
        restorePlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();
        restorePlayer(player);
    }

    private void restoreAutoDuel(Player player) {
        if (DuelSettings.getDuelSetting(player, plugin).getQueueType() == QueueType.AUTO_DUEL_ENABLED) {
            DuelSettingsGUIHandler.openGUIForPlayer(player, plugin);
        }
    }
    private void restorePlayer(Player player) {
        if (FightsHandler.isInFight(player)) {
            Player otherPlayer = plugin.getServer().getPlayer(FightsHandler.getOtherPlayer(player));
            restoreAutoDuel(player);
            restoreAutoDuel(otherPlayer);
            PlayerState.loadPlayerState(otherPlayer, true);
            PlayerState.loadPlayerState(player, true);
            FightsHandler.removeFight(player);
        }
    }

}
