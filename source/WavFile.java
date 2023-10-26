import javax.sound.sampled.*;
import java.io.*;
import java.util.ArrayList;
import java.nio.ByteBuffer;

public class WavFile {

  protected ArrayList<byte[]> byte_signal = new ArrayList<byte[]>();

  public void convertSignalToByte(ArrayList<int[]> signals) {
    for (int[] signal : signals) {
      byte[] bytes = new byte[signal.length * 4];
      int k = 0;
      for (int i=0; i < 44100; i++){
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(signal[i]);
        for (int j=0; j<4; j++){
          bytes[k] = bb.get(j);
          k+=1;
        }
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