import javax.sound.midi.*;

public class Midi {
  MidiEvent event = null; // Initialize the event to null
  ShortMessage sm = null; // Initialize the message to null
  int channel = 0;
  long tick = 0;
  int key = 0;
  int octave = 0;
  int note = 0;
  int volume = 0;
  int cmd = 0;
  int tempo = 120;

  public void infosMessage(MidiEvent event) throws InvalidMidiDataException {
    this.event = event;
    this.sm = (ShortMessage) event.getMessage();
    this.channel = sm.getChannel();
    this.tick = event.getTick();
    this.key = sm.getData1();
    this.octave = (key / 12) - 1;
    this.note = key % 12;
    this.volume = sm.getData2();
    this.cmd = sm.getCommand();
  }

  public int getTempoMorceau(MidiEvent event) {
    MetaMessage mm = (MetaMessage) event.getMessage();
    byte[] msg = mm.getMessage();
    if (((msg[1] & 0xFF) == 0x51) && (msg[2] == 0x03)) {
      int mspq = (msg[5] & 0xFF) | ((msg[4] & 0xFF) << 8) | ((msg[3] & 0xFF) << 16);
      tempo = Math.round(60000001f / mspq);
    }
    return tempo;
  }
}