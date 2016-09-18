package io.github.techno_brony.deprecated.infiniteduel.commands;

import io.github.techno_brony.deprecated.infiniteduel.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DuelCommand implements CommandExecutor {

    private final Main plugin;

    public DuelCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be run by a player!");
        }
        Player player = (Player) sender;
        plugin.listener.playersWithSelectorOpen.add(player.getUniqueId());
        if (args.length <= 0) {
            plugin.settings.get(player.getUniqueId()).autoDuel = false;
            plugin.settings.get(player.getUniqueId()).setupInventory();
            plugin.settings.get(player.getUniqueId()).selectDuel(player);
        }
        return true;
    }
}
