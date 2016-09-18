package io.github.techno_brony.infiniteduel;

import com.sun.istack.internal.Nullable;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class DuelSettings {
    private final Main plugin;

    private boolean autoDuelEnabled = false;

    private ArrayList<Kit> kitsWanted = new ArrayList<>();
    private UUID player;

    private static HashMap<UUID, DuelSettings> duelSettings = new HashMap<>();

    public DuelSettings(Main plugin, UUID player) {
        this.plugin = plugin;
        this.player = player;
    }

    @Nullable
    public static DuelSettings getDuelSetting(Player p) {
        return duelSettings.get(p.getUniqueId());
    }

    public boolean getAutoduelEnabled() {
        return autoDuelEnabled;
    }

    public void setAutoduelEnabled(boolean autoduelEnabled) {
        this.autoDuelEnabled = autoduelEnabled;
    }

    public boolean wantsKit(Kit kit) {
        return kitsWanted.contains(kit);
    }

    public void removeWantedKit(Kit kit) {
        kitsWanted.remove(kit);
    }

    public void addWantedKit(Kit kit) {
        if (!kitsWanted.contains(kit)) {
            kitsWanted.add(kit);
        }
    }
}
