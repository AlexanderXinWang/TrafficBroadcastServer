import com.iflytek.vivian.traffic.server.client.AstAbilityClient;
import com.iflytek.vivian.traffic.server.client.IatAbilityClient;
import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.EncodingAttributes;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.PrimitiveIterator;

/**
 * @ClassName AstAbilityClientTest
 * @Author xinwang41
 * @Date 2021/3/4 15:57
 **/
public class IatAbilityClientTest {
    @Autowired
    IatAbilityClient iatAbilityClient;

    @Test
    public void iatTest() throws Exception {
//        File file = new File("src\\main\\resources\\iat\\news.pcm");
        String file = "src\\main\\resources\\iat\\news.pcm";
        iatAbilityClient.iat(new File(file));
    }

}
