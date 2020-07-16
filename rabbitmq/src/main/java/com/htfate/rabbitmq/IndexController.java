package com.htfate.rabbitmq;

import com.htfate.rabbitmq.instance.pubsub.direct.DirectSender;
import com.htfate.rabbitmq.entity.vo.GoodsVO;
import com.htfate.rabbitmq.instance.pubsub.fanout.FanoutSender;
import com.htfate.rabbitmq.instance.pubsub.topic.TopicSender;
import com.htfate.rabbitmq.instance.simpe_queue.HelloSender;
import com.htfate.rabbitmq.instance.work_queue.WorkProducerA;
import com.htfate.rabbitmq.instance.work_queue.WorkProducerB;
import com.purerland.utilcenter.util.UtilsReturnMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @Autowired
    private HelloSender helloSender;

    @Autowired
    private WorkProducerA workProducerA;
    @Autowired
    private WorkProducerB workProducerB;

    @Autowired
    private FanoutSender fanoutSender;
    @Autowired
    private DirectSender directSender;
    @Autowired
    private TopicSender topicSender;

    @RequestMapping(value = "queue", method = RequestMethod.GET)
    public String sendMsg(String msg) throws Exception {

        boolean isOK = helloSender.send(msg);
        boolean is = helloSender.send(GoodsVO.builder().name("apple").size(12.2).build());

        return String.valueOf(isOK) + String.valueOf(is);
    }

    @GetMapping("work")
    public Object work(String msg,String msg2) {
        workProducerB.send(msg);
        return UtilsReturnMsg.success(workProducerA.send(msg2));
    }

    @GetMapping("fanout")
    public Object fanout(String msg) {
        return UtilsReturnMsg.success(fanoutSender.send(GoodsVO.builder().name(msg).size(12.3).build()));
    }
    @GetMapping("direct")
    public Object direct(String msg,String msg2) {
        directSender.sendDirectA(msg);
        directSender.sendDirectB(msg2);
        return UtilsReturnMsg.success();
    }
    @GetMapping("topic")
    public Object topic(String msg,String msg2) {
        topicSender.sendTopicA(msg);
        topicSender.sendTopicB(msg2);
        topicSender.sendToMatchedTopic();
        return UtilsReturnMsg.success();
    }
}
