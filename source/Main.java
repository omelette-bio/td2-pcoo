import javax.sound.midi.*;

public class Main {
  public static void main(String[] args) {
    // open a midi file to read the content, not play the music
    try {
      Sequence sequence = MidiSystem.getSequence(new java.io.File("../midi_test/fichier1.mid"));
      // get the tempo of the midi file
      Midi midi_file = new Midi();
      for (Track track : sequence.getTracks()) {
        for (int i = 0; i < track.size(); i++) {
          if (track.get(i).getMessage() instanceof MetaMessage) {
            midi_file.getTempoMorceau(track.get(i));
          }
          else {
            midi_file.addMessage(track.get(i));
          }
        }
      }
      // print the tempo of the midi file and the messages
      System.out.println("Tempo: " + midi_file.tempo);
      for (Message message : midi_file.messages) {
        System.out.println("Channel: " + message.channel);
        System.out.println("Tick: " + message.tick);
        System.out.println("Key: " + message.key);
        System.out.println("Octave: " + message.octave);
        System.out.println("Note: " + message.note);
        System.out.println("Volume: " + message.volume);
        System.out.println("Command: " + message.cmd);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
