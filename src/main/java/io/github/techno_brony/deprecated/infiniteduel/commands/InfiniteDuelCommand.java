package io.github.techno_brony.deprecated.infiniteduel.commands;

import io.github.techno_brony.deprecated.infiniteduel.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class InfiniteDuelCommand implements CommandExecutor {

    private final Main plugin;
    private final String rawHelpText = "&r&l--------------------------\n" +
            "&r&2Infinite&4Duel &5 by TechnoBrony\n" +
            "&r&6\"/duel\" &5launches duel GUI\n" +
            "&r&l--------------------------";
    private final String helpText = ChatColor.translateAlternateColorCodes('&', rawHelpText);

    public InfiniteDuelCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(helpText);
        return true;
    }
}
