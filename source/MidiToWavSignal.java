// import javax.sound.sampled.*;
// import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MidiToWavSignal {
  HashMap<Integer, Float> note_to_freq = new HashMap<Integer, Float>();
  ArrayList<Integer> signals = new ArrayList<Integer>();

  // constuctor set a HashMap to change a note into a frequence
  public MidiToWavSignal() {
    note_to_freq.put(0, 493.883f);
    note_to_freq.put(1, 466.164f);
    note_to_freq.put(2, 440.000f);
    note_to_freq.put(3, 415.305f);
    note_to_freq.put(4, 391.995f);
    note_to_freq.put(5, 369.994f);
    note_to_freq.put(6, 349.228f);
    note_to_freq.put(7, 329.628f);
    note_to_freq.put(8, 311.127f);
    note_to_freq.put(9, 293.665f);
    note_to_freq.put(10, 277.183f);
    note_to_freq.put(11, 261.626f);
  }

  // method to add a signal to the main signal ArrayList 
  private void addSignal(float frequency, int type, int nb_samples, int volume) {
    switch (type){
      case 9:
        this.whiteNoise(nb_samples, volume);
      default: 
        this.squareSignal(frequency, nb_samples, volume);
    }
  }

  // to add a square signal corresponding to the note
  private void squareSignal(float frequency, int nb_samples, int volume) {
    for (int i = 0; i < nb_samples; i++) {
      if ((i % ((float) nb_samples / frequency)) > ((float) nb_samples / 2) / frequency) {
        signals.add(volume);
      } else {
        signals.add(-volume);
      }
    }
  }

  // add random numbers to generate whitenoise
  private void whiteNoise(int nb_samples, int volume) {
    for (int i = 0; i < nb_samples; i++) {
      signals.add((new Random()).nextInt(-volume, volume+1));
    }
  }

  public void parseMidiData(ArrayList<Midi.Message> messages) {
    for (int i = 0; i < messages.size(); i++) {
      // if message's command is 144, we start the note
      if (messages.get(i).cmd == 144) {
        // substract end and start of the note (end is defined by a message with command 128) to get the duration of the note
        float duration = messages.get(i + 1).time - messages.get(i).time;
        //then we calculate how many samples the note takes
        int nb_samples = Math.round(duration * 44100);

        // calculate the frequency of the note
        float frequency =
          note_to_freq.get(messages.get(i).note) * 
          (float) Math.pow(2, messages.get(i).octave - 4);

        // finally we add the signal to the ArrayList of signals
        addSignal(frequency, messages.get(i).channel, nb_samples, messages.get(i).volume);
      }

    }
  }
}
