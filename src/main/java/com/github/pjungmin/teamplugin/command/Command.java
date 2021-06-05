package com.github.pjungmin.teamplugin.command;

import org.bukkit.command.CommandSender;

public interface Command {
    void run(CommandSender cs, String[] args);
    String getUsage();
    String getDescription();
    boolean checkPermission(CommandSender cs);
}
