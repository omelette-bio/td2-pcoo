import javax.sound.midi.*;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;

public class Midi {
  protected Sequence sequence;
  protected ArrayList<Message> messages = new ArrayList<Message>();
  protected int tempo = 120;
  protected long resolution = 0;

  class Message {
    ShortMessage sm = null;
    int channel, octave, note, volume, cmd, nb_samples;
    float time = 0;

    Message(MidiEvent event) throws InvalidMidiDataException {
      sm = (ShortMessage) event.getMessage();
      channel = sm.getChannel();
      long tick = event.getTick();

      int key = sm.getData1();
      octave = (key / 12) - 1;
      note = key % 12;

      volume = sm.getData2();
      cmd = sm.getCommand();
      time = (((float) tick) * 60 / (tempo * resolution));
    }
  }

  public void parseMidi(String midiFile) throws Error, InvalidMidiDataException, IOException {
    sequence = MidiSystem.getSequence(new File(midiFile));
    resolution = sequence.getResolution();

    for (Track track : sequence.getTracks()) {
      for (int i = 0; i < track.size(); i++) {
        if (track.get(i).getMessage() instanceof MetaMessage) {
          setTempoMorceau(track.get(i));
        } else {
          messages.add(new Message(track.get(i)));
        }
      }
    }
  }

  private void setTempoMorceau(MidiEvent event) {
    MetaMessage mm = (MetaMessage) event.getMessage();
    byte[] msg = mm.getMessage();
    if (((msg[1] & 0xFF) == 0x51) && (msg[2] == 0x03)) {
      int mspq = (msg[5] & 0xFF) 
        | ((msg[4] & 0xFF) << 8) 
        | ((msg[3] & 0xFF) << 16);
      tempo = Math.round(60000001f / mspq);
    }
  }

  public void displayAllMessages() {
    for (Message message : messages) {
      System.out.println(message.time + " " + message.cmd + " " + message.note + " " + message.octave + " "
          + message.volume + " " + message.channel);
    }
  }
}