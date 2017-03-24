package com.zeshanaslam.actionhealth;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class SettingsManager {

    public String healthMessage;
    public boolean usePerms;
    public boolean showMobs;
    public boolean showPlayers;
    public boolean delay;
    public boolean checkPvP;
    public boolean stripName;
    public boolean rememberToggle;
    public String filledHeartIcon;
    public String halfHeartIcon;
    public String emptyHeartIcon;
    public List<String> worlds = new ArrayList<>();
    public HashMap<String, String> translate = new HashMap<>();
    public List<String> regions = new ArrayList<>();
    public String mcVersion;
    public boolean useOldMethods;
    public boolean showOnLook;
    public double lookDistance;
    public List<String> blacklist = new ArrayList<>();
    public String toggleMessage;

    public SettingsManager(Main plugin) {
        // Clear settings for reloads
        worlds.clear();
        regions.clear();
        blacklist.clear();

        if (plugin.taskID != -1) Bukkit.getScheduler().cancelTask(plugin.taskID);

        // Get settings from config
        healthMessage = plugin.getConfig().getString("Health Message");
        usePerms = plugin.getConfig().getBoolean("Use Permissions");
        showMobs = plugin.getConfig().getBoolean("Show Mob");
        showPlayers = plugin.getConfig().getBoolean("Show Player");
        delay = plugin.getConfig().getBoolean("Delay Message");
        checkPvP = plugin.getConfig().getBoolean("Region PvP");
        stripName = plugin.getConfig().getBoolean("Strip Name");
        filledHeartIcon = plugin.getConfig().getString("Full Health Icon");
        halfHeartIcon = plugin.getConfig().getString("Half Health Icon");
        emptyHeartIcon = plugin.getConfig().getString("Empty Health Icon");
        if (plugin.getConfig().getBoolean("Name Change")) {
            for (String s : plugin.getConfig().getStringList("Name")) {
                String[] split = s.split(" = ");
                translate.put(split[0], split[1]);
            }
        }

        // Load disabled regions
        regions = plugin.getConfig().getStringList("Disabled regions");

        worlds = plugin.getConfig().getStringList("Disabled worlds");

        // Check if using protocol build
        mcVersion = Bukkit.getServer().getClass().getPackage().getName();
        mcVersion = mcVersion.substring(mcVersion.lastIndexOf(".") + 1);

        useOldMethods = mcVersion.equalsIgnoreCase("v1_8_R1") || mcVersion.equalsIgnoreCase("v1_7_");

        if (plugin.getConfig().contains("Remember Toggle")) {
            rememberToggle = plugin.getConfig().getBoolean("Remember Toggle");
        } else {
            rememberToggle = false;
        }

        // New options
        if (plugin.getConfig().contains("Blacklist")) {
            blacklist.addAll(plugin.getConfig().getStringList("Blacklist").stream().map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect(Collectors.toList()));
        }

        if (plugin.getConfig().contains("Show On Look")) {
            showOnLook = plugin.getConfig().getBoolean("Show On Look");
            lookDistance = plugin.getConfig().getDouble("Look Distance");

            if (showOnLook) {
                BukkitTask bukkitTask = new LookThread(plugin).runTaskTimerAsynchronously(plugin, 0, 20);
                plugin.taskID = bukkitTask.getTaskId();
            }
        } else {
            plugin.taskID = -1;
            showOnLook = false;
        }

        if (plugin.getConfig().contains("Toggle Message")) {
            toggleMessage = plugin.getConfig().getString("Toggle Message");
        }
    }
}