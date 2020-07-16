package com.htfate.rabbitmq.instance.pubsub.topic;

import com.htfate.rabbitmq.ConstantDef;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TopicReceiverA {
    @RabbitListener(queues = ConstantDef.MY_TOPICA_QUEUE)
    @RabbitHandler
    public void process(String message) {
        System.out.println("TopicReceiverA接收到的字符串消息是 => " + message);
    }
}
