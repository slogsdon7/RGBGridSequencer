package model;

import launchpad.MIDI;

import javax.sound.midi.MidiMessage;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Step implements Serializable {
    private MidiMessage sysexMessage;
    private List<Pad> pads = new ArrayList<>();
    private Sequence parent;
    private int id;
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Step(){

    }
    public Step(int padCount){
        for (int i = 0; i < padCount ; i++) {
            Pad pad = new Pad();
            pad.setIndex(i);
            pad.setParent(this);
            pads.add(pad);
        }


    }
    public MidiMessage getSysex(){
        MIDI midi = new MIDI();
        for (Pad pad : pads) {
            midi.setColorAtIndex(pad.getIndex(),pad.getColor());
        }
        return midi.getSysex();
    }

    public Sequence getParent() {
        return parent;
    }

    public void setParent(Sequence parent) {
        this.parent = parent;
    }

    public List<Pad> getPads() {
        return pads;
    }
    protected void setPads(List<Pad> pads){this.pads = pads;}

    public MidiMessage getSysexMessage() {
        return sysexMessage;
    }

    public void setSysexMessage(MidiMessage sysexMessage) {
        this.sysexMessage = sysexMessage;
    }
}

