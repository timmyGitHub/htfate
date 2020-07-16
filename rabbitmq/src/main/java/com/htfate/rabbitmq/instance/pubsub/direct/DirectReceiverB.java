package com.htfate.rabbitmq.instance.pubsub.direct;

import com.htfate.rabbitmq.ConstantDef;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DirectReceiverB {
    @RabbitListener(queues = ConstantDef.MY_DIRECTB_QUEUE)
    @RabbitHandler
    public void process(String message) {
        System.out.println("DirectReceiverB接收到的字符串消息是 => " + message);
    }
}
