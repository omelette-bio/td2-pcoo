import javax.sound.midi.InvalidMidiDataException;

public class Main {
  public static void main(String[] args) {
    try {
      Midi midi_file = new Midi();
      midi_file.parseMidi(args[0]);
      midi_file.afficherMidi();
    } catch (InvalidMidiDataException e) {
      System.err.println("error in the midi file");
    } catch (Exception e) {
      System.err.println("an error has occured");
    }
  }
}
