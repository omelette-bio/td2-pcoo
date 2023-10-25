import javax.sound.midi.*;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;

public class Midi {
  protected Sequence sequence;
  protected int tempo = 120;
  protected float time;
  protected ArrayList<Message> messages = new ArrayList<Message>();

  private class Message {
    ShortMessage sm = null; // Initialize the message to null
    int channel = 0;
    long tick = 0;
    int key = 0;
    int octave = 0;
    int note = 0;
    int volume = 0;
    int cmd = 0;
  
    public void infosMessage(MidiEvent event) throws InvalidMidiDataException {
      this.sm = (ShortMessage) event.getMessage();
      this.channel = sm.getChannel();
      this.tick = event.getTick();
      this.key = sm.getData1();
      this.octave = (key / 12) - 1;
      this.note = key % 12;
      this.volume = sm.getData2();
      this.cmd = sm.getCommand();
    }
  }

  public void parseMidi(String midiFile)  throws Error, InvalidMidiDataException, IOException {
    this.sequence = MidiSystem.getSequence(new File(midiFile));
    for (Track track : sequence.getTracks()) {
      for (int i = 0; i < track.size(); i++) {
        if (track.get(i).getMessage() instanceof MetaMessage) {
          this.setTempoTimeMorceau(track.get(i));
        } else {
          this.addMessage(track.get(i));
        }
      }
    }
  }

  private void setTempoTimeMorceau(MidiEvent event) {
    MetaMessage mm = (MetaMessage) event.getMessage();
    byte[] msg = mm.getMessage();
    if (((msg[1] & 0xFF) == 0x51) && (msg[2] == 0x03)) {
      int mspq = (msg[5] & 0xFF) | ((msg[4] & 0xFF) << 8) | ((msg[3] & 0xFF) << 16);
      this.tempo = Math.round(60000001f / mspq);
    }
    long resolution = sequence.getResolution();
    this.time = (((float) messages.get(0).tick) * 60 / (4 * tempo * resolution));
  }

  private void addMessage(MidiEvent event) throws InvalidMidiDataException {
    Message message = new Message();
    message.infosMessage(event);
    messages.add(message);
  }

  public void afficherMidi() {
    System.out.println("Tempo: " + this.tempo);
    for (Message message : this.messages) {
      System.out.println("Channel: " + message.channel + " Tick: " + message.tick + " Key: " + message.key + " Octave: " + message.octave + " Note: " + message.note + " Volume: " + message.volume + " Command: " + message.cmd);
    }
  }
}