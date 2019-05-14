package launchpad;

import javax.sound.midi.*;

import uk.co.xfactorylibrarians.coremidi4j.CoreMidiDeviceProvider;

public class Device {
    private static Receiver receiver;


    public static void send(MidiMessage msg) {
        if (receiver != null)
            receiver.send(msg, -1);
    }

    public static void setupReceiver() {
        var devices = CoreMidiDeviceProvider.getMidiDeviceInfo();
        for (MidiDevice.Info info : devices) {
            try {
                var device = MidiSystem.getMidiDevice(info);
                System.out.println(device.getDeviceInfo().getName() + " " + device.getMaxReceivers());
                if (device.getDeviceInfo().getName().contains("Launchpad Pro Standalone Port") && device.getMaxReceivers() != 0) {
                    device.open();
                    receiver = device.getReceiver();

                }
            } catch (MidiUnavailableException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
