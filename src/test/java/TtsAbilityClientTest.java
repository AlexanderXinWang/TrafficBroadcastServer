import com.iflytek.vivian.traffic.server.client.TtsAbilityClient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ClassName TtsAbilityClientTest
 * @Description TODO
 * @Author xinwang41
 * @Date 2021/4/13 16:33
 **/
public class TtsAbilityClientTest {

    @Autowired
    private TtsAbilityClient ttsAbilityClient;

    @Test
    public void ttsTest() {
        String text = "今天天气真好";
        ttsAbilityClient.tts(text, "test");
    }
}
