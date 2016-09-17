package io.github.techno_brony.infiniteduel;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Kit {
    private String name = "";
    ArrayList<ItemStack> items = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ItemStack> getItems() {
        return items;
    }

    public void setItems(ArrayList<ItemStack> items) {
        this.items = items;
    }
}
