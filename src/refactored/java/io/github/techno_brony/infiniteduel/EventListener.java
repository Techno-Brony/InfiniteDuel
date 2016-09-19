package io.github.techno_brony.infiniteduel;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        DuelSettingsGUIHandler.destroyGUI(event.getPlayer());
        DuelSettings.destroyDuelSetting(event.getPlayer());
    }

}
