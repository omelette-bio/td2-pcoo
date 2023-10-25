// import javax.sound.midi.*;

public class Main {
  public static void main(String[] args) {
    // open a midi file to read the content, not play the music
    try {
      Midi midi_file = new Midi();
      midi_file.parseMidi(args[0]);
      midi_file.afficherMidi();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
