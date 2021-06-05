package com.github.pjungmin.teamplugin.command;

import com.github.pjungmin.teamplugin.TeamPlugin;
import com.github.pjungmin.teamplugin.team.TeamDataService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static com.github.pjungmin.teamplugin.TeamPlugin.TEAM_CHAT;
import static org.bukkit.ChatColor.*;

public class CommandService {
    private final Plugin plugin;
    private final TeamDataService teamDataService;
    private final LinkedHashMap<String, Command> commandMap;

    public CommandService(Plugin plugin, TeamDataService teamDataService) {
        this.plugin = plugin;
        this.teamDataService = teamDataService;
        commandMap = new LinkedHashMap<>();
        commandMap.put("생성", new Command() {
            @Override
            public void run(CommandSender cs, String[] args) {
                Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
                if (args.length < 1) {
                    cs.sendMessage(RED + getUsage());
                    return;
                }
                Team team = scoreboard.registerNewTeam(args[0]);
                if (args.length > 1) {
                    team.setDisplayName(ChatColor.translateAlternateColorCodes('&', args[1]));
                }
                cs.sendMessage(GREEN + "성공적으로 팀을 생성했습니다.");
            }

            @Override
            public String getUsage() {
                return "/팀 생성 <팀 이름> [채팅에 표기될 팀 이름]";
            }

            @Override
            public String getDescription() {
                return "팀을 생성합니다.";
            }

            @Override
            public boolean checkPermission(CommandSender cs) {
                return cs.isOp();
            }
        });
        commandMap.put("참가", new Command() {
            @Override
            public void run(CommandSender cs, String[] args) {
                Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
                if (args.length < 2) {
                    cs.sendMessage(RED + getUsage());
                    return;
                }
                if(Bukkit.getPlayer(args[1]) == null) {
                    cs.sendMessage(RED + "존재하지 않는 플레이어입니다.");
                    return;
                }
                Team team = scoreboard.getTeam(args[0]);
                if (team == null) {
                    cs.sendMessage(RED + "존재하지 않는 팀 이름입니다.");
                } else {
                    team.addEntry(args[1]);
                    cs.sendMessage(GREEN + "성공적으로 팀에 참가시켰습니다.");
                }
            }

            @Override
            public String getUsage() {
                return "/팀 참가 <팀 이름> <닉네임>";
            }

            @Override
            public String getDescription() {
                return "<닉네임>에 해당하는 플레이어를 <팀 이름>의 팀에 넣습니다.";
            }

            @Override
            public boolean checkPermission(CommandSender cs) {
                return cs.isOp();
            }
        });
        commandMap.put("삭제", new Command() {
            @Override
            public void run(CommandSender cs, String[] args) {
                Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
                if (args.length < 1) {
                    cs.sendMessage(RED + "/팀 삭제 <팀 이름>");
                    return;
                }
                Team team = scoreboard.getTeam(args[0]);
                if (team == null) {
                    cs.sendMessage(RED + "존재하지 않는 팀 이름입니다.");
                } else {
                    team.unregister();
                    teamDataService.removeSpawnPoint(args[0]);
                    cs.sendMessage(GREEN + "성공적으로 팀을 삭제했습니다.");
                }
            }

            @Override
            public String getUsage() {
                return "/팀 삭제 <팀 이름>";
            }

            @Override
            public String getDescription() {
                return "<팀 이름>에 해당하는 팀을 삭제합니다.";
            }

            @Override
            public boolean checkPermission(CommandSender cs) {
                return cs.isOp();
            }
        });
        commandMap.put("초기화", new Command() {
            @Override
            public void run(CommandSender cs, String[] args) {
                Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
                scoreboard.getTeams().forEach(team -> {
                    team.unregister();
                    teamDataService.removeSpawnPoint(team.getName());
                });
                cs.sendMessage(GREEN + "성공적으로 모든 팀을 삭제했습니다.");
            }

            @Override
            public String getUsage() {
                return "/팀 초기화";
            }

            @Override
            public String getDescription() {
                return "생성된 모든 팀을 삭제합니다.";
            }

            @Override
            public boolean checkPermission(CommandSender cs) {
                return cs.isOp();
            }
        });
        commandMap.put("색설정", new Command() {
            @Override
            public void run(CommandSender cs, String[] args) {
                Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
                if (args.length < 2) {
                    cs.sendMessage(RED + getUsage());
                    return;
                }
                Team team = scoreboard.getTeam(args[0]);
                if (team == null) {
                    cs.sendMessage(RED + "존재하지 않는 팀 이름입니다.");
                } else {
                    ChatColor color = getByChar(args[1].charAt(args[1].length() - 1));
                    team.setColor(color);
                    System.out.println(color.name());
                    cs.sendMessage(GREEN + "색 설정이 완료되었습니다.");
                }
            }

            @Override
            public String getUsage() {
                return "/팀 색설정 <팀 이름> <색 코드(&a, &1 ...)>";
            }

            @Override
            public String getDescription() {
                return "탭리스트와 머리 위 색상을 변경합니다.";
            }

            @Override
            public boolean checkPermission(CommandSender cs) {
                return cs.isOp();
            }
        });
        commandMap.put("스폰설정", new Command() {
            @Override
            public void run(CommandSender cs, String[] args) {
                if (!(cs instanceof Player)) {
                    cs.sendMessage(RED + "해당 명령어는 콘솔에서 사용할 수 없습니다.");
                    return;
                }
                Player p = (Player) cs;
                Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
                if (args.length < 1) {
                    cs.sendMessage(RED + getUsage());
                    return;
                }
                Team team = scoreboard.getTeam(args[0]);
                if (team == null) {
                    cs.sendMessage(RED + "존재하지 않는 팀 이름입니다.");
                } else {
                    teamDataService.setSpawnPoint(args[0], p.getLocation());
                    cs.sendMessage(GREEN + "스폰 설정이 완료되었습니다.");
                }
            }

            @Override
            public String getUsage() {
                return "/팀 스폰설정 <팀 이름>";
            }

            @Override
            public String getDescription() {
                return "팀의 스폰지역을 설정합니다.";
            }

            @Override
            public boolean checkPermission(CommandSender cs) {
                return cs.isOp();
            }
        });
        commandMap.put("설정", new Command() {
            @Override
            public void run(CommandSender cs, String[] args) {
                Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
                if (args.length < 3) {
                    cs.sendMessage(RED + getUsage());
                    return;
                }
                Team team = scoreboard.getTeam(args[0]);
                if (team == null) {
                    cs.sendMessage(RED + "존재하지 않는 팀 이름입니다.");
                } else {
                    if (args[1].equals("피격무시")) {
                        team.setAllowFriendlyFire(args[2].equalsIgnoreCase("off"));
                    } else if (args[1].equals("닉네임표시")) {
                        if (args[2].equalsIgnoreCase("on")) {
                            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
                        } else if (args[2].equalsIgnoreCase("off")) {
                            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OWN_TEAM);
                        } else {
                            cs.sendMessage(RED + "/팀 설정 <팀 이름> <피격무시/닉네임표시> <on/off>");
                            return;
                        }
                        cs.sendMessage(GREEN + "성공적으로 설정이 변경되었습니다.");
                    }
                }
            }

            @Override
            public String getUsage() {
                return "/팀 설정 <팀 이름> <피격무시/닉네임표시> <on/off>";
            }

            @Override
            public String getDescription() {
                return "팀의 설정을 변경합니다.";
            }

            @Override
            public boolean checkPermission(CommandSender cs) {
                return cs.isOp();
            }
        });
        commandMap.put("채팅", new Command() {
            @Override
            public void run(CommandSender cs, String[] args) {
                if (!(cs instanceof Player)) {
                    cs.sendMessage(RED + "콘솔에서는 사용할 수 없는 명령어입니다.");
                    return;
                }
                Player p = (Player) cs;
                String mode;
                if (p.hasMetadata(TEAM_CHAT)) {
                    p.removeMetadata(TEAM_CHAT, plugin);
                    mode = GREEN + "전체 채팅";
                } else {
                    p.setMetadata(TEAM_CHAT, new FixedMetadataValue(plugin, true));
                    mode = AQUA + "팀 채팅";
                }
                cs.sendMessage(GOLD + " >> " + mode + WHITE + "모드로 변경되었습니다.");
            }

            @Override
            public String getUsage() {
                return "/팀 채팅";
            }

            @Override
            public String getDescription() {
                return "채팅 모드를 변경합니다. (팀 채팅 <-> 전체 채팅)";
            }

            @Override
            public boolean checkPermission(CommandSender cs) {
                return true;
            }
        });
    }

    public Command getCommand(String command) {
        return commandMap.get(command);
    }

    public Collection<Command> getAllCommands() {
        return commandMap.values();
    }
}
