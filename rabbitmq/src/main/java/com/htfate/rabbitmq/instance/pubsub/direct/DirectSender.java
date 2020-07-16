package com.htfate.rabbitmq.instance.pubsub.direct;

import com.htfate.rabbitmq.ConstantDef;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class DirectSender {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    public boolean sendDirectA(String message) {
        boolean isOK = false;

        if (StringUtils.isEmpty(message)) {
            System.out.println("消息为空");
            return isOK;
        }

        rabbitTemplate.convertAndSend(ConstantDef.MY_DIRECT_EXCHANGE, ConstantDef.MY_DIRECT_ROUTINGKEYA, message);

        isOK = true;

        System.out.println(String.format("DirectSender发送DirectA字符串消息结果：%s", isOK));

        return isOK;
    }

    public boolean sendDirectB(String message) {
        boolean isOK = false;

        if (StringUtils.isEmpty(message)) {
            System.out.println("消息为空");
            return isOK;
        }

        rabbitTemplate.convertAndSend(ConstantDef.MY_DIRECT_EXCHANGE, ConstantDef.MY_DIRECT_ROUTINGKEYB, message);

        isOK = true;

        System.out.println(String.format("DirectSender发送DirectB字符串消息结果：%s", isOK));

        return isOK;
    }
}
