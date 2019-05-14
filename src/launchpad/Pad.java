package launchpad;

import java.awt.*;


enum Modes{
    OFF(0),
    FLASHING(35),
    PULSING(40);

    final byte value;
    Modes(int val){
        value = (byte) val;
    }
}

public class Pad {
    boolean flashing = false;
    boolean pulsing = false;
    int index;
    Color color = new Color(0,0,0);





    }



class PadColor extends Color{
    public PadColor(int rgb) {
        super(rgb);
    }

    PadColor(){
        super(0,0,0);
    }

    @Override
    public int getRed(){
        return scale(super.getRed());

    }

    @Override
    public int getBlue(){
        return scale(super.getBlue());
    }

    @Override
    public int getGreen(){
        return scale(super.getGreen());
    }

    //utility function to reduce bitdepth of color component
    private int scale(int component){
        double scaleFactor = 64 / 255.0;
        var val = (double) component * scaleFactor;
        return (int) Math.round(val);
    }
}