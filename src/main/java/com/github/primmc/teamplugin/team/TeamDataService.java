package com.github.primmc.teamplugin.team;

import org.bukkit.Location;

import java.util.HashMap;

public class TeamDataService {
    private final HashMap<String, Location> spawnPoints;

    public TeamDataService() {
        spawnPoints = new HashMap<>();
    }

    public void setSpawnPoint(String teamName, Location spawnPoint) {
        spawnPoints.put(teamName, spawnPoint);
    }

    public Location getSpawnPoint(String teamName) {
        return spawnPoints.get(teamName);
    }

    public void removeSpawnPoint(String teamName) {
        spawnPoints.remove(teamName);
    }
}
