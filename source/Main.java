import javax.sound.midi.InvalidMidiDataException;

public class Main {
  public static void main(String[] args) {
    try {
      Midi midi_file = new Midi();
      midi_file.parseMidi(args[0]);

      MidiToWavSignal midi_to_wav_signal = new MidiToWavSignal();
      midi_to_wav_signal.parseMidiData(midi_file.messages);
      
      WavFile wav_file = new WavFile();
      wav_file.convertSignalToByte(midi_to_wav_signal.signals);
      wav_file.exportSignalToWav(wav_file.byte_signal);
    } 
    catch (InvalidMidiDataException e) {System.err.println("error in the midi file");} 
    catch (Exception e) {System.err.println("an error has occured");}
  }
}
