package com.sanjay900.nmsUtil.sk89q.jinglenote;

import java.util.HashMap;
import java.util.Map;



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

        JingleNotePlayer notePlayer = new JingleNotePlayer(player, sequencer);
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