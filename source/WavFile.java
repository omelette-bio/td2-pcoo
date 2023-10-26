import javax.sound.sampled.*;
import java.io.*;
import java.util.ArrayList;

public class WavFile {

  protected ArrayList<byte[]> byte_signal = new ArrayList<byte[]>();

  public void convertSignalToByte(ArrayList<int[]> signals) {
    for (int[] signal : signals) {
      byte[] bytes = new byte[signal.length];
      for (int i=0; i < 44100; i++){
        bytes[i] = (byte) signal[i];
      }
      this.byte_signal.add(bytes);
    }
  }

  public void exportSignalToWav() throws IOException {
    AudioFormat format = new AudioFormat(44100, 8, 1, true, true);
    FileOutputStream file = new FileOutputStream(new File("mon_fichier.wav"), true);
    for (byte[] bytes : this.byte_signal) {
      AudioInputStream ais = new AudioInputStream(new ByteArrayInputStream(bytes), format, bytes.length);
      AudioSystem.write(ais, AudioFileFormat.Type.WAVE, file);
    }
  }
}