package controller;

import model.Sequence;
import model.Step;
import views.SequencerFrame;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class SequencerController implements ChangeListener {
    private Sequence sequence;
    private SequencerFrame sequencerFrame;
    private Color color = Color.WHITE;
    private int copied;
    SequencerController(Sequence sequence) {
        this.sequence = sequence;
        sequencerFrame = new SequencerFrame("Sequencer");
        setupListeners();
        initSteps();
        sequence.sendSysex(0);
    }

    private void initSteps() {
        for (Step step : sequence.getSteps()) {
            System.out.println("step = " + step.getPosition());
            step.getPads().forEach(
                    pad -> {
                        sequencerFrame.setPadColor(
                                step.getPosition(), pad.getIndex(), pad.getColor());
                        sequencerFrame.setPadColorChangeListener(pad, step.getPosition(), pad.getIndex());
                    });

        }
    }


    public SequencerFrame getSequencerFrame() {
        return sequencerFrame;
    }

    private void setupListeners() {
        ActionListener menuListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (e.getActionCommand()){
                    case "COPY":
                       copied = sequencerFrame.getSelectedStep();
                       break;
                    case "PASTE":
                        sequence.getSteps().get(copied).getPads().forEach(pad->
                                sequencerFrame.setPadColor(pad.getIndex(),pad.getColor()));
                        sequence.sendSysex(sequencerFrame.getSelectedStep());
                        break;
                    case "DELETE":
                        sequence.delete();
                        sequencerFrame.dispose();


                }
            }
        };
        ActionListener buttonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AbstractButton btn = (AbstractButton) e.getSource();
                String cmd = e.getActionCommand();
                switch (cmd) {
                    case "PLAY":
                        sequence.Play();
                        break;
                    case "PAUSE":
                        sequence.Pause();
                        break;
                    case "STOP":
                        sequence.Stop();
                        break;
                    case "COLOR":
                        color = JColorChooser.showDialog(null, "Choose Color", Color.WHITE, false);
                        sequencerFrame.setPaintColor(color);
                        break;

                }
            }
        };
        ActionListener padListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("COPY")){
                   JMenuItem menuItem = (JMenuItem) e.getSource();
                   JPopupMenu popupMenu = (JPopupMenu) menuItem.getParent();
                    color = popupMenu.getInvoker().getBackground();
                    sequencerFrame.setPaintColor(color);
                }
                else {
                    AbstractButton btn = (AbstractButton) e.getSource();
                    btn.setBackground(color);
                    sequence.sendSysex(sequencerFrame.getSelectedStep());

                }
            }
        };
        ActionListener stepListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sequencerFrame.setSelectedStep(Integer.valueOf(e.getActionCommand()));
                sequence.sendSysex(sequencerFrame.getSelectedStep());
            }
        };
        sequencerFrame.addControlsListener(buttonListener);
        sequencerFrame.addPadListener(padListener);
        sequencerFrame.addStepListener(stepListener);
        sequencerFrame.addMenuListener(menuListener);
        sequence.addPlaybackListener(this);
    }


    @Override
    public void stateChanged(ChangeEvent e) {
        sequencerFrame.setSelectedStep(sequence.getActiveStep());
    }
}
