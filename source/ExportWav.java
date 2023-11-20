import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class ExportWav {
  HashMap<Integer, Float> note_to_freq = new HashMap<Integer, Float>();
  ArrayList<Integer> signal = new ArrayList<Integer>();

  private byte[] byte_signal;
  private String file_name;

  // constuctor set a HashMap to change a note into a frequence
  ExportWav(String file_name) {
    // set the name of the file
    Path path = Paths.get(file_name);
    this.file_name = path.getFileName().toString().replaceFirst("[.][^.]+$", "") + ".wav";
    
    // set the HashMap
    note_to_freq.put(11, 493.883f);
    note_to_freq.put(10, 466.164f);
    note_to_freq.put(9, 440.000f);
    note_to_freq.put(8, 415.305f);
    note_to_freq.put(7, 391.995f);
    note_to_freq.put(6, 369.994f);
    note_to_freq.put(5, 349.228f);
    note_to_freq.put(4, 329.628f);
    note_to_freq.put(3, 311.127f);
    note_to_freq.put(2, 293.665f);
    note_to_freq.put(1, 277.183f);
    note_to_freq.put(0, 261.626f);

  }

  // method to add a signal to the main signal ArrayList 
  private void addSignal(float frequency, Type signal, int nb_samples, int volume) {
    switch (signal){
      case NOISE:
        this.whiteNoise(nb_samples, volume);
      default: 
        this.squareSignal(frequency, nb_samples, volume);
    }
  }

  // to add a square signal corresponding to the note
  private void squareSignal(float frequency, int nb_samples, int volume) {
    for (int i = 0; i < nb_samples; i++) {
      if ((i % ( ((float) nb_samples ) / frequency)) > (( (float) nb_samples ) / 2) / frequency) {
        signal.add(volume);
      } else {
        signal.add(-(volume+1));
      }
    }
  }

  // add random numbers to generate whitenoise
  private void whiteNoise(int nb_samples, int volume) {
    for (int i = 0; i < nb_samples; i++) {
      signal.add((new Random()).nextInt(-(volume+1), volume+1));
    }
  }

  private void parseMidiData(ArrayList<Midi.Message> messages) {
    for (int i = 0; i < messages.size(); i++) {
      // if message's command is 144, we start the note
      if (messages.get(i).getCmd() == 144) {
        // create blank in the signal if there is a pause between notes
        if (i > 0) {
          float pause = messages.get(i).getTime() - messages.get(i - 1).getTime();
          int nb_samples_pause = Math.round(pause * 44100);
          for (int j = 0; j < nb_samples_pause; j++) {
            signal.add(0);
          }
        }
        // substract end and start of the note (end is defined by a message with command 128) to get the duration of the note
        float duration = messages.get(i + 1).getTime() - messages.get(i).getTime();
        //then we calculate how many samples the note takes
        int nb_samples = Math.round(duration * 44100);
        
        // calculate the frequency of the note
        float frequency =
        note_to_freq.get(messages.get(i).getNote()) * 
        (float) Math.pow(2, messages.get(i).getOctave() - 4);

        // finally we add the signal to the ArrayList of signals
        addSignal(frequency, messages.get(i).getSignal(), nb_samples, messages.get(i).getVolume());
      }
    }
  }

  public void exportSignalToWav(ArrayList<Midi.Message> messages) throws IOException {
    parseMidiData(messages);
    byte_signal = new byte[signal.size()];
    for (int i = 0; i < signal.size(); i++) {
      byte_signal[i] = signal.get(i).byteValue();
    }
    AudioFormat format = new AudioFormat(44100, 8, 1, true, true);
    AudioInputStream ais = new AudioInputStream(new
      ByteArrayInputStream(byte_signal), format, byte_signal.length);
    File file = new File(file_name);
    AudioSystem.write(ais, AudioFileFormat.Type.WAVE, file);
    
  }

  public static void main(String[] args) {
    try {
      Midi midi_file = new Midi();
      midi_file.parseMidi(args[0]);
      
      ExportWav export_wav = new ExportWav(args[0]);
      export_wav.exportSignalToWav(midi_file.getMessages());
    } 
    catch (InvalidMidiDataException e) {System.err.println("error in the midi file");} 
    catch (IOException e) {System.err.println("error in the wav file");}
    catch (ArrayIndexOutOfBoundsException e) {System.err.println("no file specified");}
    catch (Exception e) {System.err.println("error");}
  }
}
