// import javax.sound.sampled.*;
// import java.io.*;
import java.util.HashMap;
import java.util.Random;
import java.util.ArrayList;

public class MidiToWavSignal {
  HashMap<Integer, Float> note_to_freq = new HashMap<Integer, Float>();
  ArrayList<int[]> signals = new ArrayList<int[]>();

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
    public int signal[] = new int[44100];

    Signal (float frequence, String type) {
      if (type == "carre") {
        this.signalCarre(frequence);
      } else {
        this.bruitBlanc();
      }
    }

    public void signalCarre(float frequence) {
      for (int i = 0; i < 44100; i++) {
        if ((4*i % (44100f/frequence)) > 22050f/frequence) {
          this.signal[i] = 127;
        } else {
          this.signal[i] = -128;
        }
      }
    }

    public void bruitBlanc() {
      for (int i = 0; i < 44100; i++) {
        this.signal[i] = (new Random()).nextInt(-128,128);
      }
    }
  }

  public void parseMidiData(ArrayList<Midi.Message> messages) {
    for (Midi.Message message : messages) {
      switch (message.channel) {
        case 9:
          Signal signal_blanc = new Signal(note_to_freq.get(message.note), "blanc");
          signals.add(signal_blanc.signal);
          break;
        default:
          Signal signal_carre = new Signal(note_to_freq.get(message.note), "carre");
          signals.add(signal_carre.signal);
          break;
      }
    }
  }

  public void displaySignal() {
    for (int[] signal : signals) {
      for (int i = 0; i < 44100; i++) {
        System.out.println(signal[i]);
      }
    }
  }
}