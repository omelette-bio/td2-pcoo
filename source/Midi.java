import javax.sound.midi.*;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;

public class Midi {
  protected Sequence sequence;
  protected int tempo = 120;
  protected ArrayList<Message> messages = new ArrayList<Message>();

  Midi (String file) throws Error, InvalidMidiDataException, IOException {
    this.sequence = MidiSystem.getSequence(new File(file));
    for (Track track : sequence.getTracks()) {
      for (int i = 0; i < track.size(); i++) {
        if (track.get(i).getMessage() instanceof MetaMessage) {
          this.getTempoMorceau(track.get(i));
        } else {
          this.addMessage(track.get(i));
        }
      }
    }
  }

  public void getTempoMorceau(MidiEvent event) {
    MetaMessage mm = (MetaMessage) event.getMessage();
    byte[] msg = mm.getMessage();
    if (((msg[1] & 0xFF) == 0x51) && (msg[2] == 0x03)) {
      int mspq = (msg[5] & 0xFF) | ((msg[4] & 0xFF) << 8) | ((msg[3] & 0xFF) << 16);
      this.tempo = Math.round(60000001f / mspq);
    }
  }

  public void addMessage(MidiEvent event) throws InvalidMidiDataException {
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