package io.github.techno_brony.infiniteduel;

import io.github.techno_brony.infiniteduel.lib.IconMenu;
import io.github.techno_brony.infiniteduel.lib.SearchGUI;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

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
                handleItemClick(event.getName());
            }
        }, plugin);
        menu.setSpecificTo(player);
    }

    private void handleItemClick(String name) {
        if (Kit.getKit(name) != null) {
            Kit kit = Kit.getKit(name);
            if (getDuelSetting().wantsKit(kit)) {
                getDuelSetting().removeWantedKit(kit);
            } else {
                getDuelSetting().addWantedKit(kit);
            }
        }
        switch (name.toUpperCase()) {
            case "AUTODUEL ENABLED":
                getDuelSetting().disableAutoduel();
                break;

            case "AUTODUEL DISABLED":
                getDuelSetting().enableAutoduel();
                break;

            case "BEGIN FIGHT":
                getDuelSetting().beginManualDuel();
                break;

            case "WAITING FOR FIGHT":
                getDuelSetting().cancelManualDuel();
                break;

            case "YOU ARE DUELING":
                getDuelPlayerString("Type a player name");
                return;

            default:
                break;
        }
        updateInventory(getDuelSetting().getQueueType());
        menu.open(getPlayer());
    }

    private void updateInventory(QueueType queueType) {
        createInventoryBorder();
        createManualQueueItem(queueType);
        createAutoDuelItem(queueType);
        createPlayerHead(queueType, getDuelSetting().getPlayerToDuel());
        createKits();
    }

    private void createInventoryBorder() {
        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 9; x++) {
                if (y == 0 || y == 3 || y == 5 || x == 0 || x == 8) {
                    menu.setOption(y * 9 + x, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15), "");
                }
            }
        }
    }
    private void createManualQueueItem(QueueType queueType) {
        ItemStack item = getUndefinedItem();
        switch (queueType) {
            case AUTO_DUEL_ENABLED:
                item = getBlockedItem("Autoduel is Enabled", "autoduel is enabled");
                break;

            case NOT_IN_QUEUE:
                item = getItem("Begin Fight", Material.DIAMOND_SWORD, new String[] {"Click me to begin the duel!"});
                break;

            case MANUAL_DUEL_STARTED:
                item = getItem("Waiting for fight", Material.REDSTONE, new String[] {"Your duel will begin",
                        "when a player is found"});
                break;
        }
        addMenuItem(43, item);
    }
    private void createAutoDuelItem(QueueType queueType) {
        ItemStack item = getUndefinedItem();
        switch (queueType) {
            case AUTO_DUEL_ENABLED:
                item = getItem("Autoduel enabled", Material.INK_SACK, new String[] {"Your next fight will begin",
                    "when a player is available"});
                item.setDurability((short) 10);
                break;

            case NOT_IN_QUEUE:
                item = getItem("Autoduel disabled", Material.INK_SACK, new String[] {"Click here to enable autoduel"});
                item.setDurability((short) 1);
                break;

            case MANUAL_DUEL_STARTED:
                item = getBlockedItem("Waiting in queue", "you are in a queue");
                break;
        }
        addMenuItem(40, item);
    }
    private void createPlayerHead(QueueType queueType, UUID player) {
        ItemStack item = getUndefinedItem();
        switch (queueType) {
            case AUTO_DUEL_ENABLED:
                item = getBlockedItem("Autoduel is enabled", "autoduel is enabled");
                break;

            case NOT_IN_QUEUE:
                if (player == null) {
                    item = getItem("You are dueling", Material.SKULL_ITEM, new String[] {"Anyone", "Click to change"});
                } else {
                    Player p = getPlayer();
                    item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
                    SkullMeta itemMeta = (SkullMeta) item.getItemMeta();
                    itemMeta.setOwner(p.getName());
                    itemMeta.setDisplayName("You are dueling");
                    itemMeta.setLore(Arrays.asList(p.getName(), "Click to change"));
                    item.setItemMeta(itemMeta);
                }
                break;

            case MANUAL_DUEL_STARTED:
                item = getBlockedItem("Waiting in queue", "you are in a queue");
                break;
        }
        addMenuItem(37, item);
    }
    private void createKits() {
        Iterator kitsIterator = Kit.getKitsIterator();
        for (int y = 1; y <= 2; y++) {
            for (int x = 1; x <= 7; x++) {
                if (kitsIterator.hasNext()) {
                    Map.Entry kitEntry = (Map.Entry) kitsIterator.next();
                    Kit kit = (Kit) kitEntry.getValue();
                    ItemStack kitItem = kit.getItems()[0];
                    ItemMeta kitItemMeta = kitItem.getItemMeta();
                    kitItemMeta.setDisplayName(kit.getName());
                    kitItemMeta = removeAllEnchants(kitItemMeta);
                    if (getDuelSetting().wantsKit(kit)) {
                        kitItemMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
                    }
                    kitItemMeta.setLore(Arrays.asList(kit.getContentsAsString()));
                    kitItem.setItemMeta(kitItemMeta);
                    addMenuItem(y * 9 + x, kitItem);
                }
            }
        }
    }

    /**
     * Returns a barrier item
     * @param itemName The name of the item
     * @param reason The reason after 'You cannot change this option because '
     * @return An ItemStack with type Barrier
     */
    private static ItemStack getBlockedItem(String itemName, String reason) {
        String[] itemLore = {"You cannot change this", "option because", reason};
        return getItem(itemName, Material.BARRIER, itemLore);
    }

    private static ItemStack getItem(String itemName, Material material, String[] itemLore) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(itemName);
        itemMeta.setLore(Arrays.asList(itemLore));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private static ItemStack getUndefinedItem() {
        return getItem("Undefined", Material.BEDROCK, new String[] {"This item should never appear."});
    }

    private static ItemMeta removeAllEnchants(ItemMeta itemMeta) {
        for (Enchantment e : itemMeta.getEnchants().keySet()) {
            itemMeta.removeEnchant(e);
        }
        return itemMeta;
    }
    
    private DuelSettings getDuelSetting() {
        return DuelSettings.getDuelSetting(getPlayer(), plugin);
    }
    
    private Player getPlayer() {
        return plugin.getServer().getPlayer(player);
    }

    private void addMenuItem(int position, ItemStack item) {
        menu.setOption(position, item, item.getItemMeta().getDisplayName(),
                item.getItemMeta().getLore().toArray(new String[item.getItemMeta().getLore().size()]));
    }

    private void getDuelPlayerString(String itemName) {
        final SearchGUI gui = new SearchGUI(getPlayer(), new SearchGUI.AnvilClickEventHandler() {
            @Override
            public void onAnvilClick(SearchGUI.AnvilClickEvent event) {
                if (event.getSlot() == SearchGUI.AnvilSlot.OUTPUT) {
                    event.setWillClose(true);
                    event.setWillDestroy(true);
                    parseDuelPlayerString(event.getName());
                }
                event.setWillClose(false);
                event.setWillDestroy(false);
            }
        }, plugin);
        gui.setSlot(SearchGUI.AnvilSlot.INPUT_LEFT, getItem(itemName, Material.PAPER, new String[]{}));
        gui.open();
    }

    private void parseDuelPlayerString(String string) {
        Player p = plugin.getServer().getPlayer(string);
        if (p == null && !string.equalsIgnoreCase("anyone")) {
            getDuelPlayerString("Player not found");
            return;
        }
        getDuelSetting().setPlayerToDuel(p.getUniqueId());
        new BukkitRunnable(){
            @Override
            public void run() {
                menu.open(getPlayer());
            }
        }.runTaskLater(plugin, 5);
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
            playerGUIs.get(p.getUniqueId()).updateInventory(playerGUIs.get(p.getUniqueId()).getDuelSetting().getQueueType());
        }
        playerGUIs.get(p.getUniqueId()).menu.open(p);
    }

    private void destroySelf() {
        menu.destroy();
    }

    public static void destroyGUI(Player p) {
        playerGUIs.get(p.getUniqueId()).destroySelf();
        playerGUIs.remove(p.getUniqueId());
    }

}
