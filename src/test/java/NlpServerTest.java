import com.alibaba.fastjson.JSON;
import com.iflytek.vivian.traffic.server.TrafficServerApplication;
import com.iflytek.vivian.traffic.server.domain.entity.Event;
import com.iflytek.vivian.traffic.server.domain.service.NlpService;
import com.iflytek.vivian.traffic.server.dto.EventDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.bind.PrintConversionEvent;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TrafficServerApplication.class})
public class NlpServerTest {
    @Autowired
    private NlpService nlpService;

    @Test
    public void textAnalyse() {
        String source = " \"text\": \"地点是：晋阳街北美N1对面，车辆类型是：两个电动车。事件是：撞了，现在人在医院了，电动车还在现场,有人员头部受伤。事件结果是：撞的蛮严重，不清醒，在山西大医院接受治疗。\" ";
        EventDto result = nlpService.getEventCase(source);

        System.out.println("result = " + JSON.toJSONString(result));
    }
}
