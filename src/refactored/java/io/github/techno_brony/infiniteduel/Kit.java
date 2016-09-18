package io.github.techno_brony.infiniteduel;

import com.sun.istack.internal.Nullable;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

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

    public Kit(String name, ItemStack[] items) {
        this.name = name;
        this.items = items;
    }

    @Nullable
    public static Kit getKit(String name) {
        return kits.get(name);
    }

    public static void addKit(Kit kit) {
        kits.put(kit.getName(), kit);
    }
}
