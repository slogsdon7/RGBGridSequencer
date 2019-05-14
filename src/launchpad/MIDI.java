package launchpad;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.SysexMessage;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;


public class MIDI {
    private static byte[] header = {(byte)240,0,32,41,2,16,11};
    private byte[][] pads = new byte[64][4];
    private byte[] msg = new byte[header.length + pads.length + 1];
    private MidiMessage sysexMessage;
    public MIDI() {
        setPadIndices();
    }

    public void setColorAtIndex(int index, Color color){
        PadColor pc = new PadColor(color.getRGB());
        pads[index][1] = (byte) pc.getRed();
        pads[index][2] = (byte) pc.getGreen();
        pads[index][3] = (byte) pc.getBlue();
    }


    private void makeMessage() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            stream.write(header);
            for (byte[] bytes : pads) {
                stream.write(bytes);

            }
            stream.write((byte) 247);
        } catch (IOException e) {
            System.err.println(e.getMessage());

        }
        byte[] data = stream.toByteArray();

        sysexMessage = new MidiMessage(data) {
            @Override
            public Object clone() {
                return null;
            }
        };
    }


    public MidiMessage getSysex() {
        makeMessage();
        return this.sysexMessage;
    }

    private void setPadIndices(){
        for (int i = 0; i < pads.length/8; i++) {
            for (int j = 0; j<8; j++){
                pads[i*8+j][0] = (byte) ((90 - ((i+1) * 10)) + j + 1) ;
            }
        }

    }
}

