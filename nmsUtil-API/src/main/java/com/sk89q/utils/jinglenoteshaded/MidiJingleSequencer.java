// $Id$
/*
 * Tetsuuuu plugin for SK's Minecraft Server Copyright (C) 2010 sk89q <http://www.sk89q.com> All rights reserved.
 */

package com.sk89q.utils.jinglenoteshaded;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;

/**
 * A sequencer that reads MIDI files.
 *
 * @author sk89q
 */
public class MidiJingleSequencer implements JingleSequencer {

    private static final int[] instruments = {
        0, 0, 0, 0, 0, 0, 0, 5, // 8
        6, 0, 0, 0, 0, 0, 0, 0, // 16
        0, 0, 0, 0, 0, 0, 0, 5, // 24
        5, 5, 5, 5, 5, 5, 5, 5, // 32
        6, 6, 6, 6, 6, 6, 6, 6, // 40
        5, 5, 5, 5, 5, 5, 5, 2, // 48
        5, 5, 5, 5, 0, 0, 0, 0, // 56
        0, 0, 0, 0, 0, 0, 0, 0, // 64
        0, 0, 0, 0, 0, 0, 0, 0, // 72
        0, 0, 0, 0, 0, 0, 0, 0, // 80
        0, 0, 0, 0, 0, 0, 0, 0, // 88
        0, 0, 0, 0, 0, 0, 0, 0, // 96
        0, 0, 0, 0, 0, 0, 0, 0, // 104
        0, 0, 0, 0, 0, 0, 0, 0, // 112
        1, 1, 1, 3, 1, 1, 1, 5, // 120
        1, 1, 1, 1, 1, 2, 4, 3, // 128
    };


    private static final int[] percussion = {
        3, 3, 4, 4, 3, 2, 3, 2, //8 - Electric Snare
        2, 2, 2, 2, 2, 2, 2, 2, //16 - Hi Mid Tom
        3, 2, 3, 3, 3, 0, 3, 3, //24 - Cowbell
        3, 3, 3, 2, 2, 3, 3, 3, //32 - Low Conga
        2, 2, 0, 0, 2, 2, 0, 0, //40 - Long Whistle
        3, 3, 3, 3, 3, 3, 5, 5, //48 - Open Cuica
        3, 3,                   //50 - Open Triangle
    };

    protected final File midiFile;
    private Sequencer sequencer = null;
    private boolean running = false;
    private boolean playedBefore = false;

    private Set<JingleNotePlayer> players = new HashSet<JingleNotePlayer>();

    public MidiJingleSequencer(File midiFile, boolean loop) throws MidiUnavailableException, InvalidMidiDataException, IOException {

        this.midiFile = midiFile;

        try {
            sequencer = MidiSystem.getSequencer(false);
            sequencer.open();
            Sequence seq = MidiSystem.getSequence(midiFile);
            sequencer.setSequence(seq);
            if(loop)
                sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
        } catch (MidiUnavailableException e) {
            stop();
            throw e;
        } catch (InvalidMidiDataException e) {
            stop();
            throw e;
        } catch (IOException e) {
            stop();
            throw e;
        }
    }

    @Override
    public void run() throws InterruptedException {

        final Map<Integer, Integer> patches = new HashMap<Integer, Integer>();

        try {
            if(sequencer == null || sequencer.getSequence() == null)
                return;

            if (!sequencer.isOpen())
                sequencer.open();

            sequencer.getTransmitter().setReceiver(new Receiver() {

                @Override
                public void send(MidiMessage message, long timeStamp) {

                    if(players.isEmpty()) {
                        running = false;
                        return;
                    }

                    if ((message.getStatus() & 0xF0) == ShortMessage.PROGRAM_CHANGE) {

                        ShortMessage msg = (ShortMessage) message;
                        int chan = msg.getChannel();
                        int patch = msg.getData1();
                        patches.put(chan, patch);
                    } else if ((message.getStatus() & 0xF0) == ShortMessage.NOTE_ON) {

                        ShortMessage msg = (ShortMessage) message;
                        int chan = msg.getChannel();
                        int n = msg.getData1();
                        synchronized(players) {
                            if (chan == 9) {
                                    for(JingleNotePlayer player : players)
                                        player.play(new Note(toMCSound(toMCPercussion(patches.get(chan))), toMCNote(n),  10 * (msg.getData2() / 127f)));
                            } else {
                                for(JingleNotePlayer player : players)
                                    player.play(new Note(toMCSound(toMCInstrument(patches.get(chan))), toMCNote(n), 10 * (msg.getData2() / 127f)));
                            }
                        }
                    }
                }

                @Override
                public void close() {
                    running = false;
                }
            });

            try {
                if (sequencer.isOpen()) {
                    sequencer.start();
                    running = true;
                    playedBefore = true;
                } else
                    throw new IllegalArgumentException("Sequencer is not open!");
            } catch(Exception e){
            	e.printStackTrace();
            }
        } catch (MidiUnavailableException e) {
        	e.printStackTrace();
        }
    }

    @Override
    public void stop() {

        if(!running) return;
        players.clear();
        if (sequencer != null) {
            try {
                if(sequencer.isRunning())
                    sequencer.stop();
                if(sequencer.isOpen())
                    sequencer.close();
                sequencer = null;
            } catch(Exception e){}
        }
        running = false;
    }

    protected static byte toMCNote(int n) {

        if (n < 54) return (byte) ((n - 6) % (18 - 6));
        else if (n > 78) return (byte) ((n - 6) % (18 - 6) + 12);
        else return (byte) (n - 54);
    }

    protected static byte toMCInstrument(Integer patch) {

        if (patch == null) return 0;

        if (patch < 0 || patch >= instruments.length) return 0;

        return (byte) instruments[patch];
    }

    protected Instrument toMCSound(byte instrument) {

        switch (instrument) {
            case 1:
                return Instrument.BASS_GUITAR;
            case 2:
                return Instrument.SNARE_DRUM;
            case 3:
                return Instrument.STICKS;
            case 4:
                return Instrument.BASS_DRUM;
            case 5:
                return Instrument.GUITAR;
            case 6:
                return Instrument.BASS;
            default:
                return Instrument.PIANO;
        }
    }

    protected static byte toMCPercussion(Integer patch) {

        if(patch == null)
            return 0;

        int i = patch - 33;
        if (i < 0 || i >= percussion.length) {
            return 1;
        }

        return (byte) percussion[i];
    }

    public Sequencer getSequencer() {
        return sequencer;
    }

    @Override
    public boolean isPlaying () {

        return running && sequencer != null;
    }

    @Override
    public boolean hasPlayedBefore () {
        return playedBefore;
    }

    @Override
    public void stop (JingleNotePlayer player) {
        players.remove(player);
        if(players.isEmpty()) {
            stop();
        }
    }

    @Override
    public void play (JingleNotePlayer player) {
        players.add(player);
        if(!playedBefore)
            try {
                run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }

    @Override
    public Set<JingleNotePlayer> getPlayers () {
        return players;
    }
}