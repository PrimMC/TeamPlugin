package com.github.pjungmin.teamplugin;

import com.github.pjungmin.teamplugin.command.CommandListener;
import com.github.pjungmin.teamplugin.command.CommandService;
import com.github.pjungmin.teamplugin.listener.TeamEventListeners;
import com.github.pjungmin.teamplugin.team.TeamDataService;
import org.bukkit.plugin.java.JavaPlugin;

public class TeamPlugin extends JavaPlugin {
    public static final String TEAM_CHAT = "TEAM_CHAT";

    private TeamDataService teamDataService;
    private CommandService commandService;

    private CommandListener commandListener;
    private TeamEventListeners teamEventListeners;

    @Override
    public void onEnable() {
        teamDataService = new TeamDataService();
        commandService = new CommandService(this, teamDataService);

        commandListener = new CommandListener(commandService);
        teamEventListeners = new TeamEventListeners(getServer().getScoreboardManager().getMainScoreboard(), teamDataService);
        getCommand("팀").setExecutor(commandListener);
        getServer().getPluginManager().registerEvents(teamEventListeners, this);
    }
}
