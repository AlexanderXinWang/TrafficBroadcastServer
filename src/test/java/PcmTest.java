
import com.iflytek.vivian.traffic.server.client.TtsAbilityClient;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;


public class PcmTest {

    @Autowired
    TtsAbilityClient ttsAbilityClient;

    private static float RATE = 16000;
    //编码格式PCM
    private static AudioFormat.Encoding ENCODING = AudioFormat.Encoding.PCM_SIGNED;
    //帧大小 16
    private static int SAMPLE_SIZE = 16;
    //是否大端
    private static boolean BIG_ENDIAN = false;
    //通道数
    private static int CHANNELS = 1;  //1单通道  2双通道

    //从声卡中采集的数据
    public static void functionName() throws LineUnavailableException {
        AudioFormat audioFormat = new AudioFormat(ENCODING, RATE, SAMPLE_SIZE, CHANNELS, (SAMPLE_SIZE / 8) * CHANNELS,
                RATE, BIG_ENDIAN);
        TargetDataLine targetDataLine = AudioSystem.getTargetDataLine(audioFormat);
        targetDataLine.open();
        targetDataLine.start();
        byte[] b = new byte[1024 * 100];
        int flag = 0;
        while ((flag = targetDataLine.read(b, 0, b.length)) > 0) {
            //b：就是产生的字节流

        }
    }



    /**
     * WAV转PCM文件
     * @param wavfilepath wav文件路径
     * @param pcmfilepath pcm要保存的文件路径及文件名
     * @return
     */
    public static String convertAudioFiles(String wavfilepath,String pcmfilepath){
        FileInputStream fileInputStream;
        FileOutputStream fileOutputStream;
        try {
            fileInputStream = new FileInputStream(wavfilepath);
            fileOutputStream = new FileOutputStream(pcmfilepath);
            byte[] wavbyte = InputStreamToByte(fileInputStream);
            byte[] pcmbyte = Arrays.copyOfRange(wavbyte, 44, wavbyte.length);
            fileOutputStream.write(pcmbyte);
            IOUtils.closeQuietly(fileInputStream);
            IOUtils.closeQuietly(fileOutputStream);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return pcmfilepath;
    }
    /**
     * 输入流转byte二进制数据
     * @param fis
     * @return
     * @throws IOException
     */
    private static byte[] InputStreamToByte(FileInputStream fis) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        long size = fis.getChannel().size();
        byte[] buffer = null;
        if (size <= Integer.MAX_VALUE) {
            buffer = new byte[(int) size];
        } else {
            buffer = new byte[8];
            for (int ix = 0; ix < 8; ++ix) {
                int offset = 64 - (ix + 1) * 8;
                buffer[ix] = (byte) ((size >> offset) & 0xff);
            }
        }
        int len;
        while ((len = fis.read(buffer)) != -1) {
            byteStream.write(buffer, 0, len);
        }
        byte[] data = byteStream.toByteArray();
        IOUtils.closeQuietly(byteStream);
        return data;
    }


    @Test
    public void test() {
//        String wavFilePath="C:\\Users\\AlexanderWang\\Desktop\\TrafficBroadcastSystem\\testVoice\\录音.wav";
//        String pcmFilePath="C:\\Users\\AlexanderWang\\Desktop\\TrafficBroadcastSystem\\testVoice\\test.pcm";
//        convertAudioFiles(wavFilePath,pcmFilePath);
//        System.out.println("OK");

        String text = "实时，" + "静园东路48号" + "，发生事件" + "两车相撞"
                + "，车辆类型为" + "家用轿车" + "，事件结果是" + "人在医院";

        ttsAbilityClient.tts(text, "test");
    }

}
