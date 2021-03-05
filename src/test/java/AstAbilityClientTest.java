import com.iflytek.vivian.traffic.server.client.AstAbilityClient;
import com.iflytek.vivian.traffic.server.utils.AssembleAuthUrlUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.PrimitiveIterator;

/**
 * @ClassName AstAbilityClientTest
 * @Author xinwang41
 * @Date 2021/3/4 15:57
 **/
public class AstAbilityClientTest {
    @Autowired
    private AssembleAuthUrlUtil assembleAuthUrlUtil;
    @Value("${ability.ast.hostUrl}")
    private String hostUrl;
    @Value("${ability.ast.host}")
    private String host;
    @Value("${ability.ast.path}")
    private String path;
    @Value("${ability.ast.apiKey}")
    private String apikey;
    @Value("${ability.ast.apiSecret}")
    private String apiSecret;

    @Test
    public void generateUrlTest() throws InvalidKeyException, NoSuchAlgorithmException {
        String astUrl = assembleAuthUrlUtil.assembleAuthUrl(hostUrl,host,path,apiSecret,apikey);
        System.out.println(astUrl);
    }
}
