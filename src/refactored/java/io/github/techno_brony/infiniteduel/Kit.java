package io.github.techno_brony.infiniteduel;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Kit {
    private String name;
    private ItemStack[] items;

    private static HashMap<String, Kit> kits = new HashMap<>();

    public String getName() {
        return name;
    }

    public ItemStack[] getItems() {
        return items;
    }

    public String[] getContentsAsString() {
        String[] contents = new String[items.length];
        for (int i = 0; i < contents.length; i++) {
            ItemStack itemStack = items[i];
            contents[i] = " - " + itemStack.getAmount() + " " + itemStack.getType().toString();
        }
        return contents;
    }

    public Kit(String name, ItemStack[] items) {
        this.name = name;
        this.items = items;
    }

    public static Kit getKit(String name) {
        return kits.get(name);
    }

    public static Iterator<Map.Entry<String,Kit>> getKitsIterator() {
        return kits.entrySet().iterator();
    }

    public static void addKit(Kit kit) {
        kits.put(kit.getName(), kit);
    }
}
