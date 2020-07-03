package de.benned.classes;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Check - Analyses scaled values of clickspeed.
 * 
 * Created on 08.04.2018, 15:24
 * @author benned (ben.eggert@gmx.net)
 * 
 * Copyright (C) 2018
 */

public class Check {

    private static final int MAX_CLICKSPEED = 8;
    private static final int TOO_CONSISTENT_CLICKSPEED = 5;
    private static final int MAX_REVISION = 3;

    /**
     * Checks whether the clickspeed is too similar.
     * @param player Player which is supposed to be checked.
     * @return True, if the last value of player's clickspeed revisions is greater than MAX_REVISION, otherwise false.
     */

    public static boolean suspectedByClickConsistency(Player player) {

        if (Main.getClickMap().get(player).get(0) < TOO_CONSISTENT_CLICKSPEED) {
            return false;
        }
        if (Main.getRevisionMap().get(player) < MAX_REVISION) {
            return false;
        }
        return true;
    }

    /**
     * Checks whether the clickspeed in one second is too fast.
     * @param player Player which is supposed to be checked.
     * @return True, if the last value of player's clickspeed is greater than MAX_CLICKSPEED, otherwise false.
     */

    public static boolean suspectedByClickSpeed(Player player) {

        return Main.getClickMap().get(player).get(0) >= MAX_CLICKSPEED;
    }

    /**
     * Sends a clickspeed warning message to all authorized players with permissions.
     * @param suspected Staff members should be notified about this player.
     * @param clickspeed Player's scaled clickspeed.
     */

    public static void sendClickSpeedMessage(Player suspected, int clickspeed) {

        for (Player player : Bukkit.getOnlinePlayers()) {

            if (player.hasPermission("clickspeed.notify")) {

                player.sendMessage(Main.PREFIX + "§7Player §e" + suspected.getName() + " §7has strange clickspeed: §e" + clickspeed + " §7CPS.");
            }
        }
    }

    /**
     * Sends a consistency warning message to all authorized players with permissions.
     * @param suspected Staff members should be notified about this player.
     * @param revisions Player's scaled revisions of same clickspeed.
     * @param clickspeed Player's scaled clickspeed.
     */

    public static void sendRevisionsMessage(Player suspected, int revisions, int clickspeed) {

        for (Player player : Bukkit.getOnlinePlayers()) {

            if (player.hasPermission("clickspeed.notify")) {

                player.sendMessage(Main.PREFIX + "§7Player §e" + suspected.getName() + " §7has very consistent clickspeed: §e" + revisions + " §7revisions, §e" + clickspeed + " §7CPS.");
            }
        }
    }

    /**
     * Validates whether the clickspeed of a player has changed.
     * @param player Player which is supposed to be checked.
     * @return True, if the clickspeed of the player has changed, otherwise false.
     */

    public static boolean hasClickspeedChanged(Player player) {

        return !Main.getClickMap().get(player).get(0).equals(Main.getClickMap().get(player).get(1));
    }
}
