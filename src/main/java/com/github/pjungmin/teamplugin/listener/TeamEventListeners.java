package com.github.pjungmin.teamplugin.listener;

import com.github.pjungmin.teamplugin.TeamPlugin;
import com.github.pjungmin.teamplugin.team.TeamDataService;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.github.pjungmin.teamplugin.TeamPlugin.TEAM_CHAT;
import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.RESET;

@RequiredArgsConstructor
public class TeamEventListeners implements Listener {
    private final Scoreboard scoreboard;
    private final TeamDataService teamDataService;

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        Team team = scoreboard.getEntryTeam(p.getName());
        if(team == null) return;
        if(p.hasMetadata(TEAM_CHAT)) {
            Set<Player> recipients = team.getEntries().stream()
                    .map(s -> Bukkit.getPlayer(s))
                    .filter(Objects::nonNull).collect(Collectors.toSet());
            e.getRecipients().clear();
            e.getRecipients().addAll(recipients);
            e.setFormat(GRAY  + "[팀] " + RESET + e.getFormat());
        } else e.setFormat(team.getDisplayName() + " " + e.getFormat());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        Team team = scoreboard.getEntryTeam(p.getName());
        if(team == null) return;
        Location spawnPoint = teamDataService.getSpawnPoint(team.getName());
        if(spawnPoint == null) return;
        System.out.println(spawnPoint);
        System.out.println(team);
        e.setRespawnLocation(spawnPoint);
    }
}
