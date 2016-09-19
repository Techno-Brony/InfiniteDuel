package io.github.techno_brony.infiniteduel.commands;

import io.github.techno_brony.infiniteduel.DuelSettingsGUIHandler;
import io.github.techno_brony.infiniteduel.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DuelCommand implements CommandExecutor {

    private static final String COMMAND_NAME = "duel";
    private final Main plugin;

    public DuelCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            DuelSettingsGUIHandler.openGUIForPlayer((Player) commandSender, plugin);
        } else {
            commandSender.sendMessage(ChatColor.RED + "This command can only be run by a player!");
        }
        return true;
    }

    public static String getCommandName() {
        return COMMAND_NAME;
    }

}
