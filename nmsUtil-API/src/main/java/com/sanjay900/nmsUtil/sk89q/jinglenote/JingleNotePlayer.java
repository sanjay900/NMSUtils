package com.sanjay900.nmsUtil.sk89q.jinglenote;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.sanjay900.nmsUtil.sk89q.jinglenote.JingleSequencer.Note;

public class JingleNotePlayer implements Runnable {

    protected final String player;
    protected JingleSequencer sequencer;
    Player p = null;
    /**
     * Constructs a new JingleNotePlayer
     * 
     * @param player The player who is hearing this's name.
     * @param seq The JingleSequencer to play.
     * @param area The SearchArea for this player. (optional)
     */
    public JingleNotePlayer(String player, JingleSequencer seq) {

        this.player = player;
        sequencer = seq;
    }

    @Override
    public void run() {

        if(sequencer == null)
            return;
        try {
            try {
                sequencer.play(this);
            } catch (Throwable t) {
                t.printStackTrace();
            }

            while(isPlaying()){
                Thread.sleep(10L);
            }
        } catch (InterruptedException e) {
        	e.printStackTrace();
        } finally {
            stop();
        }
    }

    public boolean isPlaying() {

    	if (p == null || !p.isOnline()) {
            p = Bukkit.getPlayerExact(player);
        }
        return !(p == null || !p.isOnline()) &&(sequencer != null && (sequencer.isPlaying() || !sequencer.hasPlayedBefore()));
    }

    public String getPlayer() {

        return player;
    }

    public void stop() {

        if (sequencer != null) {
            sequencer.stop(this);
            sequencer = null;
        }
    }

    public void play (Note note)  {

        if(!isPlaying()) return;

        p.playSound(p.getLocation(), toSound(note.getInstrument()), note.getVelocity(), note.getNote());
    }
    public Sound toSound(Instrument instrument) {

        switch(instrument) {
            case PIANO:
                return Sound.NOTE_PIANO;
            case GUITAR:
                return Sound.NOTE_PLING;
            case BASS:
                return Sound.NOTE_BASS;
            case BASS_GUITAR:
                return Sound.NOTE_BASS_GUITAR;
            case STICKS:
                return Sound.NOTE_STICKS;
            case BASS_DRUM:
                return Sound.NOTE_BASS_DRUM;
            case SNARE_DRUM:
                return Sound.NOTE_SNARE_DRUM;
            default:
                return Sound.NOTE_PIANO;
        }
    }
}