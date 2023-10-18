import javax.sound.midi.*;
import java.util.ArrayList;

public class Midi {
  protected int tempo = 120;
  protected ArrayList<Message> messages = new ArrayList<Message>();

  public int getTempoMorceau(MidiEvent event) {
    MetaMessage mm = (MetaMessage) event.getMessage();
    byte[] msg = mm.getMessage();
    if (((msg[1] & 0xFF) == 0x51) && (msg[2] == 0x03)) {
      int mspq = (msg[5] & 0xFF) | ((msg[4] & 0xFF) << 8) | ((msg[3] & 0xFF) << 16);
      tempo = Math.round(60000001f / mspq);
    }
    return tempo;
  }

  public void addMessage(MidiEvent event) throws InvalidMidiDataException {
    Message message = new Message();
    message.infosMessage(event);
    messages.add(message);
  }
}