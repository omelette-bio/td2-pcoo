import java.util.Random;

public class Signal {
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