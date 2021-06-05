package com.github.pjungmin.teamplugin.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

import static org.bukkit.ChatColor.*;

@RequiredArgsConstructor
public class CommandListener implements CommandExecutor {
    private final CommandService commandService;

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        if (args.length > 0) {
            Command command = commandService.getCommand(args[0]);
            if(command == null) sendCommandHelp(sender);
            else if (command.checkPermission(sender)) {
                command.run(sender, Arrays.copyOfRange(args, 1, args.length));
            }else sender.sendMessage(RED + "권한이 없습니다.");
        }else {
            sendCommandHelp(sender);
        }
        return true;
    }

    private void sendCommandHelp(CommandSender sender) {
        commandService.getAllCommands().forEach(command -> {
            if(command.checkPermission(sender)) {
                sender.sendMessage(GOLD + command.getUsage() + WHITE + " : " + command.getDescription());
            }
        });
    }
}
