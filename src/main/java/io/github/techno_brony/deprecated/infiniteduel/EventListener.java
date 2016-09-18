package io.github.techno_brony.deprecated.infiniteduel;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class EventListener implements Listener {
    private final Main plugin;
    public ArrayList<UUID> playersWithSelectorOpen = new ArrayList<>();

    EventListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        cleanUpFight(p);
        if (plugin.inFightAutoDuel.contains(p.getUniqueId())) {
            plugin.inFightAutoDuel.remove(p.getUniqueId());
        }
        if (plugin.queue.containsKey(p.getUniqueId())) {
            plugin.queue.remove(p.getUniqueId());
        }
        plugin.settings.remove(p.getUniqueId());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        final Player p = event.getEntity().getPlayer();
        cleanUpFight(p);
        if (plugin.inFightAutoDuel.contains(p.getUniqueId())) {
            plugin.getLogger().log(Level.INFO, event.getEntity().getPlayer().getDisplayName()); //TODO REMOVE
            plugin.settings.get(p.getUniqueId()).autoDuel = true;
            checkForFight(event.getEntity().getPlayer());
            new BukkitRunnable() {
                @Override
                public void run() {
                    plugin.settings.get(p.getUniqueId()).selectDuel(p);
                }
            }.runTaskLater(plugin, 20);
            plugin.inFightAutoDuel.remove(p.getUniqueId());
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
                if (plugin.queue.containsKey(clicker.getUniqueId())) return;
                if (plugin.settings.get(clicker.getUniqueId()).kitsWanted.contains(plugin.kits.get(itemDisplayName))) {
                    plugin.settings.get(clicker.getUniqueId()).kitsWanted.remove(plugin.kits.get(itemDisplayName));
                } else {
                    plugin.settings.get(clicker.getUniqueId()).kitsWanted.add(plugin.kits.get(itemDisplayName));
                }
            } else {
                switch (itemDisplayName) {
                    case "Toggle Autoduel":
                        if (!plugin.settings.get(clicker.getUniqueId()).autoDuel) {
                            if (plugin.queue.containsKey(clicker.getUniqueId())) {
                                return;
                            }
                            plugin.settings.get(clicker.getUniqueId()).autoDuel = true;
                            checkForFight((Player) clicker);
                        } else {
                            if (!plugin.lookingForArena.contains(clicker.getUniqueId())) {
                                plugin.queue.remove(clicker.getUniqueId());
                                plugin.settings.get(clicker.getUniqueId()).autoDuel = false;
                            }
                        }
                        break;

                    case "Begin!":
                        checkForFight((Player) clicker);
                        break;

                    case "Waiting in queue...":
                        plugin.queue.remove(clicker.getUniqueId());
                        break;

                    case "You are dueling":
                        if (plugin.queue.containsKey(clicker.getUniqueId())) return;
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
        final HumanEntity player = event.getPlayer();
        if (!plugin.lookingForArena.contains(player.getUniqueId()) &&
                plugin.settings.get(player.getUniqueId()).autoDuel) {
            playersWithSelectorOpen.remove(event.getPlayer().getUniqueId());
            plugin.queue.remove(player.getUniqueId());
            plugin.settings.get(player.getUniqueId()).autoDuel = false;
        }
        if (plugin.queue.containsKey(event.getPlayer().getUniqueId())) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    plugin.settings.get(player.getUniqueId()).selectDuel((Player) player);
                }
            }.runTaskLater(plugin, 5);
            return;
        }
        if (playersWithSelectorOpen.contains(event.getPlayer().getUniqueId())) {
            playersWithSelectorOpen.remove(event.getPlayer().getUniqueId());
            plugin.settings.get(event.getPlayer().getUniqueId()).autoDuel = false;
            plugin.getLogger().log(Level.INFO, event.getPlayer().getName() + " inventory close open delete duel"); //TODO REMOVE
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.settings.put(event.getPlayer().getUniqueId(), new DuelSettingsSelector(plugin));
    }

    private void checkForFight(final Player player) {
        for (Kit wantedKit : plugin.settings.get(player.getUniqueId()).kitsWanted) {
            Iterator it = plugin.queue.entrySet().iterator();
            while (it.hasNext()) {
                final Map.Entry tempEntry = (Map.Entry) it.next();
                ArrayList<String> tempList = (ArrayList<String>) tempEntry.getValue();
                for (final String s : tempList) {
                    if (tempList.contains(wantedKit.getName())) {
                        plugin.lookingForArena.add(player.getUniqueId());
                        plugin.preFightState.put(player.getUniqueId(), new PreFightState(player));
                        for (Arena arena : plugin.arenas) {
                            if (!arena.isInUse()) {
                                plugin.lookingForArena.remove(player.getUniqueId());
                                if (plugin.settings.get(player.getUniqueId()).autoDuel) {
                                    plugin.inFightAutoDuel.add(player.getUniqueId());
                                    plugin.getLogger().log(Level.INFO, player.getDisplayName() + " in loop"); //TODO REMOVE
                                }
                                startFight(player, plugin.getServer().getPlayer((UUID) tempEntry.getKey()),
                                        plugin.kits.get(s), arena);
                                return;
                            }
                        }
                        new LookForArena(plugin, player, tempEntry, s).runTaskTimer(plugin, 20, 10 * 20);
                        return;
                    }
                }

            }
        }
        plugin.preFightState.put(player.getUniqueId(), new PreFightState(player));
        ArrayList<String> kitsWantedStrings = new ArrayList<>();
        for (Kit wantedKit : plugin.settings.get(player.getUniqueId()).kitsWanted) {
            kitsWantedStrings.add(wantedKit.getName());
        }
        plugin.queue.put(player.getUniqueId(), kitsWantedStrings);
    }

    void startFight(Player first, Player second, Kit kit, Arena arena) {
        arena.setInUse(true);
        plugin.playerInArena.put(first.getUniqueId(), arena);
        plugin.playerInArena.put(second.getUniqueId(), arena);

        preparePlayerForFight(first);
        preparePlayerForFight(second);

        if (arena.getSpawnLocations().size() < 2) {
            plugin.getLogger().log(Level.WARNING, "Invalid Spawn Locations");
        }

        loadFightKitArena(first, kit, arena.getSpawnLocations().get(0));
        loadFightKitArena(second, kit, arena.getSpawnLocations().get(1));

        plugin.currentFights.put(first.getUniqueId(), second.getUniqueId());
        plugin.currentFights.put(second.getUniqueId(), first.getUniqueId());
    }

    private void preparePlayerForFight(Player p) {
        p.getInventory().clear();
        p.setFoodLevel(20);
        p.setSaturation(20.0f);
        p.setHealth(20.0f);
        p.setExp(0.0f);
        p.setGameMode(GameMode.ADVENTURE);
        plugin.queue.remove(p.getUniqueId());
    }

    private void loadFightKitArena(Player player, Kit kit, Location location) {
        player.teleport(location);
        player.getInventory().setContents(kit.getItems().toArray(new ItemStack[kit.getItems().size()]));
    }

    private void cleanUpFight(Player p) {
        if (plugin.currentFights.containsKey(p.getUniqueId())) { //TODO Loser doesnt get teleported back
            UUID value = plugin.currentFights.get(p.getUniqueId());
            Player s = plugin.getServer().getPlayer(value);

            plugin.preFightState.get(p.getUniqueId()).updateFromState(p);
            plugin.preFightState.get(s.getUniqueId()).updateFromState(s);

            plugin.preFightState.remove(p.getUniqueId());
            plugin.preFightState.remove(s.getUniqueId());

            plugin.currentFights.remove(p.getUniqueId());
            plugin.currentFights.remove(s.getUniqueId());

            plugin.playerInArena.get(value).setInUse(false);
            plugin.playerInArena.get(p.getUniqueId()).setInUse(false);

            plugin.playerInArena.remove(value);
            plugin.playerInArena.remove(p.getUniqueId());
        }
    }

}
