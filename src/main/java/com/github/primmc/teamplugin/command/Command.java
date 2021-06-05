package com.github.primmc.teamplugin.command;

import org.bukkit.command.CommandSender;

public interface Command {
    void run(CommandSender cs, String[] args);
    String getUsage();
    String getDescription();
    boolean checkPermission(CommandSender cs);
}
