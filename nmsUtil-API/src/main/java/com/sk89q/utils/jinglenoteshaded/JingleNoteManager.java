// $Id$
/*
 * Tetsuuuu plugin for SK's Minecraft Server Copyright (C) 2010 sk89q <http://www.sk89q.com> All rights reserved.
 */

package com.sk89q.utils.jinglenoteshaded;

import java.util.HashMap;
import java.util.Map;

import com.sk89q.utils.jinglenoteshaded.bukkit.BukkitJingleNotePlayer;



/**
 * A manager of play instances.
 *
 * @author sk89q
 */
public class JingleNoteManager {

    /**
     * List of instances.
     */
    protected final Map<String, JingleNotePlayer> instances = new HashMap<String, JingleNotePlayer>();

    public boolean isPlaying(String player) {

        return instances.containsKey(player) && instances.get(player).isPlaying();
    }

    public boolean isPlaying() {

        /*if(instances.isEmpty()) return false;
        Iterator<String> iter = instances.keySet().iterator();
        while(iter.hasNext()) {
            String ent = iter.next();
            if(!isPlaying(ent))
                stop(ent);
        }*/
        return !instances.isEmpty();
    }

    public void play(String player, JingleSequencer sequencer) {

        // Existing player found!
        if (instances.containsKey(player)) {
            stop(player);
        }

        JingleNotePlayer notePlayer = new BukkitJingleNotePlayer(player, sequencer);
        Thread thread = new Thread(notePlayer);
        thread.setDaemon(true);
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.setName("JingleNotePlayer for " + player);
        thread.start();

        instances.put(player, notePlayer);
    }

    public boolean stop(String player) {

        // Existing player found!
        if (instances.containsKey(player)) {
            instances.remove(player).stop();
            return true;
        }
        return false;
    }

    public void stopAll() {

        for (JingleNotePlayer notePlayer : instances.values()) {
            notePlayer.stop();
        }

        instances.clear();
    }
}