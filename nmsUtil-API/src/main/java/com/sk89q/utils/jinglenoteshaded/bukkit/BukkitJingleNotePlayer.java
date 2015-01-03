package com.sk89q.utils.jinglenoteshaded.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.sk89q.utils.jinglenoteshaded.Instrument;
import com.sk89q.utils.jinglenoteshaded.JingleNotePlayer;
import com.sk89q.utils.jinglenoteshaded.JingleSequencer;
import com.sk89q.utils.jinglenoteshaded.JingleSequencer.Note;


public class BukkitJingleNotePlayer extends JingleNotePlayer {

    public BukkitJingleNotePlayer (String player, JingleSequencer seq) {
        super(player, seq);
    }

    Player p = null;

    @Override
    public void play (Note note)  {

        if(!isPlaying()) return;

        p.playSound(p.getLocation(), toSound(note.getInstrument()), note.getVelocity(), note.getNote());
    }

    @Override
    public boolean isPlaying() {

        if (p == null || !p.isOnline()) {
            p = Bukkit.getPlayerExact(player);
        }
        return !(p == null || !p.isOnline()) && super.isPlaying();

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