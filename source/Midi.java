import javax.sound.midi.*;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;

// this class is used to parse a midi file and store all the messages
public class Midi {
  protected ArrayList<Message> messages = new ArrayList<Message>();
  protected int tempo = 120;
  protected long resolution = 0;
  private float cur_time = 0, prec_time = 0;

  // this class is used to store a message's data
  public class Message {
    protected int octave, note, volume, cmd;
    protected float time = 0;
    Type signalType;
    protected int tempo_msg;
  
    Message(MidiEvent event) throws InvalidMidiDataException {
      ShortMessage sm = (ShortMessage) event.getMessage();
      tempo_msg = tempo;

      // sound calculation
      int key = sm.getData1();
      octave = (key / 12) - 1;
      note = key % 12;
  
      int channel = sm.getChannel();
  
      switch(channel) {
        case 9:
          signalType = Type.NOISE;
          break;
        default:
          signalType = Type.SQUARE;
          break;
      }
  
      // volume calculation
      volume = sm.getData2();
  
      time = ( (((float) event.getTick()) * 60) / (tempo * resolution));
  
      float bkup = time;
      
      if (time < prec_time) { time = prec_time; }
      cur_time += time - prec_time;
      time = cur_time;
      prec_time = bkup;

      // command calculation, check if the note is starting or ending
      cmd = sm.getCommand();
    }
  }

  public void parseMidi(String midiFile) throws Error, InvalidMidiDataException, IOException {
    Sequence sequence = MidiSystem.getSequence(new File(midiFile));
    resolution = sequence.getResolution();
    

    //loop through the midi file and store all the messages
    for (Track track : sequence.getTracks()) {
      for (int i = 0; i < track.size(); i++) {
        if (track.get(i).getMessage() instanceof MetaMessage) {
          setTempoMorceau(track.get(i));
        } else if (track.get(i).getMessage() instanceof ShortMessage){
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
          + message.volume + " " + message.signalType + " " + message.tempo_msg);
    }
  }
}