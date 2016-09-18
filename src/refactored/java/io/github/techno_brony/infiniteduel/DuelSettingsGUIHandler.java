package io.github.techno_brony.infiniteduel;

import io.github.techno_brony.infiniteduel.lib.IconMenu;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class DuelSettingsGUIHandler {
    private static final String GUI_NAME = "Configure your Duel";
    private static final int GUI_SIZE = 6 * 9;

    private final Main plugin;
    private IconMenu menu;
    private UUID player;

    private static HashMap<UUID, DuelSettingsGUIHandler> playerGUIs = new HashMap<>();

    private DuelSettingsGUIHandler(Main plugin, Player player) {
        this.player = player.getUniqueId();
        this.plugin = plugin;
        menu = new IconMenu(GUI_NAME, GUI_SIZE, new IconMenu.OptionClickEventHandler() {
            @Override
            public void onOptionClick(IconMenu.OptionClickEvent event) {
                handleItemClick(event.getName(), event.getItem());
            }
        }, plugin);
        menu.setSpecificTo(player);
    }

    private void handleItemClick(String name, ItemStack item) {
        if (Kit.getKit(name) != null) {
            //TODO Enable or Disable the kit
            return;
        }
        switch (name.toUpperCase()) {
            case "AUTODUEL ENABLED":
                //TODO disable AutoDuel
                break;

            case "AUTODUEL DISABLED":
                //TODO enable AutoDuel
                break;

            case "BEGIN!":
                //TODO begin manual duel
                break;

            case "WAITING IN QUEUE...":
                //TODO cancel manual duel
                break;

            default:
                break;
        }

    }

    private void updateInventory(boolean autoDuelEnabled, boolean manualDuelEnabled) {

    }

    /**
     * Returns the GUI for this player
     * If not available one is created
     * @param p The player to get the GUI for
     * @param plugin Plugin used for instantiating a new GUI
     */
    public static void openGUIForPlayer(Player p, Main plugin) {
        if (!playerGUIs.containsKey(p.getUniqueId())) {
            playerGUIs.put(p.getUniqueId(), new DuelSettingsGUIHandler(plugin, p));
        }
        playerGUIs.get(p.getUniqueId()).menu.open(p);
    }

}
