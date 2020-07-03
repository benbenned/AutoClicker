package de.benned.classes;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * ClickListener - Observes any events.
 *
 * Created on 09.04.2018, 15:02
 * @author benned (ben.eggert@gmx.net)
 *
 * Copyright (C) 2018
 */

public class ClickListener implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent event) {

        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {

            Player player = event.getPlayer();

            HashMap<Player, List<Integer>> clicks = Main.getClickMap();
            HashMap<Player, Integer> revisions = Main.getRevisionMap();
            HashMap<Player, Boolean> running = Main.getRunningMap();

            if (running.get(player)) {

                // clicking while scaling clickspeed.

                synchronized (clicks) {

                    clicks.get(player).set(0, clicks.get(player).get(0) + 1);
                }
            } else {

                running.replace(player, true);

                synchronized (clicks) {

                    clicks.get(player).set(1, clicks.get(player).get(0));
                    clicks.get(player).set(0, 1);
                }

                Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {

                    running.replace(player, false);

                    Integer currentClicks = 0;

                    synchronized (clicks) {

                        currentClicks = clicks.get(player).get(0);
                    }

                    synchronized (revisions) {

                        if (!Check.hasClickspeedChanged(player)) {

                            revisions.replace(player, revisions.get(player) + 1);
                        } else {

                            revisions.replace(player, 0);
                        }
                    }

                    if (Check.suspectedByClickConsistency(player)) {

                        Check.sendRevisionsMessage(player, revisions.get(player), currentClicks);
                    }

                    if (Check.suspectedByClickSpeed(player)) {

                        Check.sendClickSpeedMessage(player, currentClicks);
                    }
                }, 20L);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

        if (Main.getClickMap().containsKey(event.getPlayer())) {

            Player player = event.getPlayer();

            try {
                Main.getClickMap().remove(player);
                Main.getRevisionMap().remove(player);
                Main.getRunningMap().remove(player);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        Main.getRunningMap().put(player, false);
        Main.getRevisionMap().put(player, 0);

        List<Integer> newList = new ArrayList<>(Arrays.asList(0, 0));

        Main.getClickMap().put(player, newList);
    }
}
