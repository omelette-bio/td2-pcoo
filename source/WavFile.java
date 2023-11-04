import javax.sound.sampled.*;
import java.io.*;
import java.util.ArrayList;

public class WavFile {

  protected byte[] byte_signal;

  public void convertSignalToByte(ArrayList<Integer> signals) {
    byte_signal = new byte[signals.size()];
    for (int i = 0; i < signals.size(); i++) {
      byte_signal[i] = signals.get(i).byteValue();
    }
  }

  public void exportSignalToWav() throws IOException {
    AudioFormat format = new AudioFormat(44100, 8, 1, true, true);
    AudioInputStream ais = new AudioInputStream(new
      ByteArrayInputStream(byte_signal), format, byte_signal.length);
    File file = new File("mon_fichier.wav");
    AudioSystem.write(ais, AudioFileFormat.Type.WAVE, file);
  }
}