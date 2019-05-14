package model;

import launchpad.Device;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Sequence implements Serializable {
    private volatile double tempo = 180.0;
    private int length;
    private int id;
    private Project parent;
    private List<Step> steps;
    private List<ChangeListener> listeners = new ArrayList<>();

    public Project getParent() {
        return parent;
    }

    public void setParent(Project parent) {
        this.parent = parent;
    }

    transient private boolean playing = false;
    transient private int step = 1;
    public Sequence(){
        length = 16;
        steps = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            Step s = new Step(64);
            s.setPosition(i);
            s.setParent(this);
            steps.add(s);
        }
    }
    public int getID(){
        return id;
    }
    protected void setID(int id){
        this.id = id;
    }
    public int getLength() {
        return length;
    }
    public int getActiveStep(){return step;}
    public void setActiveStep(int step){
        this.step = step;
    }
    public List<Step> getSteps() {
        return steps;
    }
    public void setSteps(List<Step> steps){this.steps = steps;}
    public void delete(){
        DB.delete(this);
    }
    public void sendSysex(int step){
        Device.send(steps.get(step).getSysex());

    }
    public void Play(){
        playing = true;
        doPlayback();
    }
    public void Pause(){
        playing = false;
    }
    public void Stop(){
        playing = false;
        step = 0;
        ChangeEvent e = new ChangeEvent(this);
        listeners.forEach(l -> l.stateChanged(e));
    }

    public void addPlaybackListener(ChangeListener changeListener){
        listeners.add(changeListener);
    }
    private void advanceStep(){
        step++;
        step = step%length;
        Device.send(steps.get(step).getSysex());
        ChangeEvent e = new ChangeEvent(this);
        listeners.forEach(l -> l.stateChanged(e));
    }
    private void doPlayback(){

        Thread t = new Thread(){
            public void run(){
                while(playing){
                    try{
                        advanceStep();
                        Thread.sleep((long)((60/tempo)*1000));
                    }
                    catch (InterruptedException e){
                        System.err.println(e.getMessage());
                    }
                }
                interrupt();
            }
        };
        t.start();

    }



}
