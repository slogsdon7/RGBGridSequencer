package model;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;

public class Pad implements Serializable, PropertyChangeListener {
    private Color color = Color.DARK_GRAY;
    private boolean flashing = false;
    private boolean pulsing = false;
    private int id;
    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private Step parent;

    public Step getParent() {
        return parent;
    }

    public void setParent(Step parent) {
        this.parent = parent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isFlashing() {
        return flashing;
    }

    public void setFlashing(boolean flashing) {
        this.flashing = flashing;
    }

    public boolean isPulsing() {
        return pulsing;
    }

    public void setPulsing(boolean pulsing) {
        this.pulsing = pulsing;
    }

    public Pad(){

    }

    @Override
    //background
    public void propertyChange(PropertyChangeEvent evt) {
        color = (Color) evt.getNewValue();
        System.out.println("evt = [" + evt + "]");
    }
}
