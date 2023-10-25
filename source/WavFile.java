import javax.sound.sampled.*;
import java.io.*;
import java.util.ArrayList; // Au début du fichier
import java.nio.ByteBuffer; // Au début du fichier

public class WavFile {

  protected ArrayList<byte[]> byte_signal = new ArrayList<byte[]>();

  public void convertSignalToByte(ArrayList<int[]> signals) {
    for (int[] signal : signals) {
      byte[] bytes = new byte[signal.length * 4];
      for (int i=0; i < 44100; i++){
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(signal[i]);
        for (int j=0; j<4; j++){
          bytes[i] = bb.get(j);
        }
      }
      this.byte_signal.add(bytes);
    }
  }

  public void exportSignalToWav(ArrayList<byte[]> sig_bytes) throws IOException{
    AudioFormat format = new AudioFormat(44100, 8, 1, true, true);
    File file = new File("mon_fichier.wav");
    for (byte[] bytes : sig_bytes){
      AudioInputStream ais = new AudioInputStream(new ByteArrayInputStream(bytes), format, bytes.length);
      AudioSystem.write(ais, AudioFileFormat.Type.WAVE, file);
    }
  }
}