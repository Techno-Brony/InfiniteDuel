package io.github.techno_brony.infiniteduel.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class InfiniteDuelCommand implements CommandExecutor {
    private static final String COMMAND_NAME = "infiniteduel";

    private final String rawHelpText = "&aInfiniteDuel &c1.0 &dby TechnoBrony\n" +
            "&r&eCommands:\n" +
            "&r&7- &b\"/duel\" &dlaunches the duel GUI.\n" +
            "&7- &b\"/infiniteduel\" &dshows this help text.";
    private final String helpText = ChatColor.translateAlternateColorCodes('&', rawHelpText);

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        commandSender.sendMessage(helpText);
        return false;
    }

    public static String getCommandName() {
        return COMMAND_NAME;
    }
}
