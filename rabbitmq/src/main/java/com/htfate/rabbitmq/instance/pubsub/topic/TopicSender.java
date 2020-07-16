package com.htfate.rabbitmq.instance.pubsub.topic;

import com.htfate.rabbitmq.ConstantDef;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class TopicSender {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    public boolean sendTopicA(String message) {
        boolean isOK = false;

        if (StringUtils.isEmpty(message)) {
            System.out.println("消息为空");
            return isOK;
        }

        rabbitTemplate.convertAndSend(ConstantDef.MY_TOPIC_EXCHANGE, ConstantDef.MY_TOPIC_ROUTINGKEYA, message);

        isOK = true;

        System.out.println(String.format("TopicSender发送TopicA字符串消息结果：%s", isOK));

        return isOK;
    }

    public boolean sendTopicB(String message) {
        boolean isOK = false;

        if (StringUtils.isEmpty(message)) {
            System.out.println("消息为空");
            return isOK;
        }

        rabbitTemplate.convertAndSend(ConstantDef.MY_TOPIC_EXCHANGE, ConstantDef.MY_TOPIC_ROUTINGKEYB, message);

        isOK = true;

        System.out.println(String.format("TopicSender发送TopicB字符串消息结果：%s", isOK));

        return isOK;
    }

    public boolean sendToMatchedTopic() {

        boolean isOK = false;

        String routingKey = "my topic routingkeya.16";//模糊匹配ConstantDef.MY_TOPIC_ROUTINGKEYA

        //String routingKey = "my_topic_routingkeyB.32";//模糊匹配ConstantDef.MY_TOPIC_ROUTINGKEYB

        String matchedKeys = "";
        if (ConstantDef.MY_TOPIC_ROUTINGKEYA.contains(routingKey.split("\\.")[0])) {
            matchedKeys = "my topic routingkeya";
        } else if (ConstantDef.MY_TOPIC_ROUTINGKEYB.contains(routingKey.split("\\.")[0])) {
            matchedKeys = "my topic routingkeyb";
        }

        String msg = "message to matched receivers:" + matchedKeys;

        rabbitTemplate.convertAndSend(ConstantDef.MY_TOPIC_EXCHANGE, routingKey, msg);

        isOK = true;

        System.out.println(String.format("TopicSender发送字符串消息结果：%s", isOK));

        return isOK;
    }
}
