import javax.sound.sampled.*;
import java.io.*;
import java.util.ArrayList;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WavFile {

  protected byte[] byte_signal;
  protected String file_name;

  public WavFile(String file_name) {
    Path path = Paths.get(file_name);
    this.file_name = path.getFileName().toString().replaceFirst("[.][^.]+$", "") + ".wav";
  }

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
    File file = new File(file_name);
    AudioSystem.write(ais, AudioFileFormat.Type.WAVE, file);
  }
}