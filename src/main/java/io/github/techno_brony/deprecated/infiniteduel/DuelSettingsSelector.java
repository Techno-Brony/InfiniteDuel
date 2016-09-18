package io.github.techno_brony.deprecated.infiniteduel;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class DuelSettingsSelector {

    private final Main plugin;
    private Inventory duelSettingsInventory = Bukkit.createInventory(null, 54, "Configure your Duel");
    public ArrayList<Kit> kitsWanted = new ArrayList<>();
    public boolean autoDuel = false;
    private UUID playerUUID;

    public DuelSettingsSelector(Main plugin) {
        this.plugin = plugin;
        setupInventory();
    }

    public Inventory getDuelSettingsInventory() {
        return duelSettingsInventory;
    }

    public void setupInventory() {
        Iterator it = plugin.kits.entrySet().iterator();
        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 9; x++) {
                if (x == 0 || x == 8 || y == 0 || y == 5 || y == 3) {
                    duelSettingsInventory.setItem(y * 9 + x, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
                    continue;
                }

                if (it.hasNext()) {
                    if (plugin.queue.containsKey(playerUUID)) {

                    } else {
                        Map.Entry pair = (Map.Entry) it.next();
                        Kit tempKit = (Kit) pair.getValue();
                        ItemStack itemStack = new ItemStack(tempKit.getItems().get(0));
                        ItemMeta tempMeta = itemStack.getItemMeta();
                        for (Map.Entry enchantment : itemStack.getEnchantments().entrySet()) {
                            itemStack.removeEnchantment((Enchantment) enchantment.getKey());
                        }
                        tempMeta.setDisplayName(tempKit.getName());
                        if (kitsWanted.contains(tempKit)) {
                            tempMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
                            ArrayList<String> lore = new ArrayList<>();
                            lore.add("Enabled");
                            tempMeta.setLore(lore);
                        }
                        itemStack.setItemMeta(tempMeta);
                        duelSettingsInventory.setItem(y * 9 + x, itemStack);
                    }
                }
            }
        }

        if (autoDuel) {
            ItemStack noBeginItem = new ItemStack(Material.BARRIER);
            ItemMeta noBeginItemMeta = noBeginItem.getItemMeta();
            noBeginItemMeta.setDisplayName("Autoduel Enabled");
            ArrayList<String> noBeginItemLore = new ArrayList<>();
            noBeginItemLore.add("You cannot start a match");
            noBeginItemLore.add("manually when autoduel is enabled");
            noBeginItemMeta.setLore(noBeginItemLore);
            noBeginItem.setItemMeta(noBeginItemMeta);
            duelSettingsInventory.setItem(43, noBeginItem);
        } else {
            if (plugin.queue.containsKey(playerUUID)) {
                ItemStack beginItem = new ItemStack(Material.REDSTONE);
                ItemMeta beginItemMeta = beginItem.getItemMeta();
                beginItemMeta.setDisplayName("Waiting in queue...");
                ArrayList<String> beginItemLore = new ArrayList<>();
                beginItemLore.add("Your match will start");
                beginItemLore.add("when a player is found");
                beginItemMeta.setLore(beginItemLore);
                beginItem.setItemMeta(beginItemMeta);
                duelSettingsInventory.setItem(43, beginItem);
            } else {
                ItemStack beginItem = new ItemStack(Material.DIAMOND_SWORD);
                ItemMeta beginItemMeta = beginItem.getItemMeta();
                beginItemMeta.setDisplayName("Begin!");
                ArrayList<String> beginItemLore = new ArrayList<>();
                beginItemLore.add("When you are ready, click me!");
                beginItemMeta.setLore(beginItemLore);
                beginItem.setItemMeta(beginItemMeta);
                duelSettingsInventory.setItem(43, beginItem);
            }
        }

        ItemStack autoDuelItem;
        if (autoDuel) {
            autoDuelItem = new ItemStack(Material.INK_SACK, 1, (short) 10);
        } else {
            autoDuelItem = new ItemStack(Material.INK_SACK, 1, (short) 1);
        }
        ItemMeta autoDuelItemMeta = autoDuelItem.getItemMeta();
        autoDuelItemMeta.setDisplayName("Toggle Autoduel");
        ArrayList<String> autoDuelItemLore = new ArrayList<>();
        if (autoDuel) {
            autoDuelItemLore.add("Your next duel will start");
            autoDuelItemLore.add("when a player is available");
        } else {
            autoDuelItemLore.add("Disabled");
        }
        autoDuelItemMeta.setLore(autoDuelItemLore);
        autoDuelItem.setItemMeta(autoDuelItemMeta);
        duelSettingsInventory.setItem(40, autoDuelItem);

        if (autoDuel) {
            ItemStack noPlayerItem = new ItemStack(Material.BARRIER);
            ItemMeta noPlayerItemMeta = noPlayerItem.getItemMeta();
            noPlayerItemMeta.setDisplayName("Autoduel Enabled");
            ArrayList<String> noPlayerItemLore = new ArrayList<>();
            noPlayerItemLore.add("You cannot duel a specific");
            noPlayerItemLore.add("player when autoduel is enabled");
            noPlayerItemMeta.setLore(noPlayerItemLore);
            noPlayerItem.setItemMeta(noPlayerItemMeta);
            duelSettingsInventory.setItem(37, noPlayerItem);
        } else {
            ItemStack playerItem = new ItemStack(Material.SKULL_ITEM);
            ItemMeta playerItemMeta = playerItem.getItemMeta();
            playerItemMeta.setDisplayName("You are dueling");
            ArrayList<String> playerItemLore = new ArrayList<>();
            playerItemLore.add("Anyone"); //TODO Change to suit actual player
            playerItemLore.add("Click to change");
            playerItemMeta.setLore(playerItemLore);
            playerItem.setItemMeta(playerItemMeta);
            duelSettingsInventory.setItem(37, playerItem);
        }

        ItemStack infoItem = new ItemStack(Material.SIGN);
        ItemMeta infoItemMeta = infoItem.getItemMeta();
        infoItemMeta.setDisplayName("Information");
        ArrayList<String> infoItemLore = new ArrayList<>();
        infoItemLore.add("Click a kit to select it");
        infoItemLore.add("The kit you use will be chosen");
        infoItemLore.add("from your selection");
        infoItemMeta.setLore(infoItemLore);
        infoItem.setItemMeta(infoItemMeta);
        duelSettingsInventory.setItem(4, infoItem);

        if (autoDuel) {
            ItemStack warnAutoItem = new ItemStack(Material.REDSTONE_TORCH_ON);
            ItemMeta warnInfoItemMeta = warnAutoItem.getItemMeta();
            warnInfoItemMeta.setDisplayName("Warning");
            ArrayList<String> warnInfoItemLore = new ArrayList<>();
            warnInfoItemLore.add("Exiting this inventory");
            warnInfoItemLore.add("will disable autoduel");
            warnInfoItemMeta.setLore(warnInfoItemLore);
            warnAutoItem.setItemMeta(warnInfoItemMeta);
            duelSettingsInventory.setItem(49, warnAutoItem);
        }

    }

    public void selectDuel(Player player) {
        playerUUID = player.getUniqueId();
        player.openInventory(this.duelSettingsInventory);
    }

}
