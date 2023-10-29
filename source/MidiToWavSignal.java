// import javax.sound.sampled.*;
// import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MidiToWavSignal {

  HashMap<Integer, Float> note_to_freq = new HashMap<Integer, Float>();
  ArrayList<Integer> signals = new ArrayList<Integer>();

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

  private class Signal {

    Signal(float frequence, String type, int nb_samples, int volume) {
      if (type == "carre") {
        this.signalCarre(frequence, nb_samples, volume);
      } else {
        this.bruitBlanc(nb_samples, volume);
      }
    }

    public void signalCarre(float frequence, int nb_samples, int volume) {
      for (int i = 0; i < nb_samples; i++) {
        if (
          (4 * i % ((float) nb_samples / frequence)) >
          ((float) nb_samples / 2) /
          frequence
        ) {
          signals.add(volume);
        } else {
          signals.add(-volume);
        }
      }
    }

    public void bruitBlanc(int nb_samples, int volume) {
      for (int i = 0; i < nb_samples; i++) {
        signals.add((new Random()).nextInt(-volume, volume+1));
      }
    }
  }

  public void parseMidiData(ArrayList<Midi.Message> messages) {
    int len = messages.size();
    for (int i = 0; i < len; i++) {
      if (messages.get(i).cmd == 144) {
        float duree = messages.get(i + 1).time - messages.get(i).time;
        int nb_samples = Math.round(duree * 44100);
        float frequence =
          note_to_freq.get(messages.get(i).note) *
          (float) Math.pow(2, messages.get(i).octave - 4);
        switch (messages.get(i).channel) {
          case 9:
            new Signal(frequence, "blanc", nb_samples, messages.get(i).volume);
          default:
            new Signal(frequence, "carre", nb_samples, messages.get(i).volume);
        }
      }
    }
  }
}
