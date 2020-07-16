package com.htfate.rabbitmq.instance.pubsub.topic;

import com.htfate.rabbitmq.ConstantDef;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TopicReceiverB {
    @RabbitListener(queues = ConstantDef.MY_TOPICB_QUEUE)
    @RabbitHandler
    public void process(String message) {
        System.out.println("TopicReceiverB接收到的字符串消息是 => " + message);
    }
}
