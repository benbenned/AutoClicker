package de.benned.classes;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;

/**
 * Main - Manages all methods.
 * 
 * Created on 08.04.2018, 14:56
 * @author benned (ben.eggert@gmx.net)
 * 
 * Copyright (C) 2018
 */

public class Main extends JavaPlugin {

    public static final String PREFIX = "§7[§cAntiCheat§7] ";

    private static Main instance;
    private static HashMap<Player, List<Integer>> clickMap;
    private static HashMap<Player, Integer> revisionMap;
    private static HashMap<Player, Boolean> runningMap;

    public void onEnable() {

        instance = this;
        clickMap = new HashMap<>();
        revisionMap = new HashMap<>();
        runningMap = new HashMap<>();


        init();
    }

    private void init() {

        Bukkit.getPluginManager().registerEvents(new ClickListener(), instance);
    }

    public static Main getInstance() {
        return instance;
    }

    public static HashMap<Player, List<Integer>> getClickMap() {
        return clickMap;
    }

    public static HashMap<Player, Integer> getRevisionMap() {
        return revisionMap;
    }

    public static HashMap<Player, Boolean> getRunningMap() {
        return runningMap;
    }
}
