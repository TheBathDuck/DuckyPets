package dev.thebathduck.duckypets.utils.command;

import dev.thebathduck.duckypets.Duckypets;
import dev.thebathduck.duckypets.utils.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractCommand implements TabExecutor {
    public CommandSender sender;

    public Command command;

    public String commandName;

    public Argument[] arguments;

    public AbstractCommand(String commandName) {
        this.commandName = commandName;
        this.arguments = new Argument[0];
    }

    public AbstractCommand(String commandName, Argument... arguments) {
        this.commandName = commandName;
        this.arguments = arguments;
    }

    public void register(Duckypets plugin) {
        register(plugin, Boolean.FALSE);
    }

    public void register(Duckypets plugin, Boolean needsPermission) {
        PluginCommand cmd = plugin.getCommand(this.commandName);
        if (cmd != null) {
            if (needsPermission) {
                cmd.setPermission(getBasePermission());
                cmd.setPermissionMessage(Util.color("&cTo use this command you need permission."));
            }
            cmd.setExecutor(this);
        }
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.command = command;
        this.sender = sender;
        execute(sender, command, label, args);
        return true;
    }

    public abstract void execute(CommandSender sender, Command command, String label, String[] args);

    protected boolean senderIsPlayer() {
        return this.sender instanceof org.bukkit.entity.Player;
    }

    protected boolean hasPermission(String permission, boolean silent) {
        if (this.sender.hasPermission(permission))
            return true;
        if (!silent)
            this.sender.sendMessage(Util.color("&cTo use this command you need permission"));
        return false;
    }

    protected void addIfPermission(CommandSender sender, Collection<String> options, String permission, String option) {
        if (sender.hasPermission(permission))
            options.add(option);
    }

    protected String getBasePermission() {
        return "duckypets." + this.commandName;
    }

    protected void sendNotEnoughArguments(AbstractCommand command) {
        this.sender.sendMessage(Util.color("&6/" + command.command.getName() + " <subcommand> <arg>..."));
        for (Argument argument : this.arguments) {
            if (argument.getPermission() == null || this.sender.hasPermission(argument.getPermission())) {
                String description = (argument.getDescription() == null) ? "No description" : argument.getDescription();
                this.sender.sendMessage(Util.color("&a/" + command.command.getName() + " &2" + argument.getArguments() + "&f - &a" + description));
            }
        }
    }

    protected List<String> getApplicableTabCompleters(String arg, Collection<String> completions) {
        return (List<String>) StringUtil.copyPartialMatches(arg, completions, new ArrayList(completions.size()));
    }
}