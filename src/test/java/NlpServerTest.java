import com.alibaba.fastjson.JSON;
import com.iflytek.vivian.traffic.server.TrafficServerApplication;
import com.iflytek.vivian.traffic.server.domain.dao.IEventDao;
import com.iflytek.vivian.traffic.server.domain.entity.Event;
import com.iflytek.vivian.traffic.server.domain.service.NlpService;
import com.iflytek.vivian.traffic.server.dto.EventDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TrafficServerApplication.class})
public class NlpServerTest {
    @Autowired
    private NlpService nlpService;

    @Autowired
    IEventDao eventDao;

    @Test
    public void textAnalyse() {
        String source = " \"text\": \"地点是望江西路666号，车辆类型是两个电动车，事件是两车相撞，现在人在医院，电动车在现场，有人员头部受伤，事件结果是撞的挺严重，不清醒，在医院接受治疗。\" ";
        String source2 = " \"text\": \"地点是习友路，事件是两车相撞，车辆类型是家用轿车，事件结果是人在医院。\"";
        EventDto result = nlpService.getEventCase(source2);
        System.out.println(result);
        System.out.println("result = " + JSON.toJSONString(result));
    }

    @Test
    public void isPlay() {
        List<Event> eventList = eventDao.findEventsByIsPlay("0");
        System.out.println(eventList);
    }
}
