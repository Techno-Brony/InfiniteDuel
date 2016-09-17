package io.github.techno_brony.infiniteduel;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.UUID;

public class EventListener implements Listener {
    private final Main plugin;
    public ArrayList<UUID> playersWithSelectorOpen = new ArrayList<>();

    EventListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (plugin.inFightAutoDuel.contains(event.getPlayer().getUniqueId())) {
            //TODO
        }
        plugin.settings.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        //TODO
        if (plugin.inFightAutoDuel.contains(event.getEntity().getUniqueId())) {
            
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        HumanEntity clicker = event.getWhoClicked();
        if (playersWithSelectorOpen.contains(clicker.getUniqueId())) {
            event.setCancelled(true);
        } else {
            return;
        }
        try {
            String itemDisplayName = event.getCurrentItem().getItemMeta().getDisplayName();
            if (plugin.kits.containsKey(itemDisplayName)) {
                if (plugin.settings.get(clicker.getUniqueId()).kitsWanted.contains(plugin.kits.get(itemDisplayName))) {
                    plugin.settings.get(clicker.getUniqueId()).kitsWanted.remove(plugin.kits.get(itemDisplayName));
                } else {
                    plugin.settings.get(clicker.getUniqueId()).kitsWanted.add(plugin.kits.get(itemDisplayName));
                }
            } else {
                switch (itemDisplayName) {
                    case "Toggle Autoduel":
                        plugin.settings.get(clicker.getUniqueId()).autoDuel = !plugin.settings.get(clicker.getUniqueId()).autoDuel;
                        break;

                    case "Begin!":

                        break;

                    case "You are dueling":
                        event.getWhoClicked().sendMessage("Not Implemented Yet!"); //TODO
                        break;

                    default:
                        break;
                }
            }
            plugin.settings.get(clicker.getUniqueId()).setupInventory();
        } catch (NullPointerException e) {}
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (playersWithSelectorOpen.contains(event.getPlayer().getUniqueId())) {
            playersWithSelectorOpen.remove(event.getPlayer().getUniqueId());
            plugin.settings.get(event.getPlayer().getUniqueId()).autoDuel = false;
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.settings.put(event.getPlayer().getUniqueId(), new DuelSettingsSelector(plugin));
    }

}
