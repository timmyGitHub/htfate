package com.htfate.rabbitmq.instance.pubsub.direct;

import com.htfate.rabbitmq.ConstantDef;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DirectReceiverA {
    @RabbitListener(queues = ConstantDef.MY_DIRECTA_QUEUE)
    @RabbitHandler
    public void process(String message) {
        System.out.println("DirectReceiverA接收到的字符串消息是 => " + message);
    }
}
