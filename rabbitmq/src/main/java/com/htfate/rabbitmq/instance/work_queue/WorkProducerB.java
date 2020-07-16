package com.htfate.rabbitmq.instance.work_queue;

import com.htfate.rabbitmq.ConstantDef;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class WorkProducerB {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    public boolean send(String message) {
        boolean isOK = false;

        if (StringUtils.isEmpty(message)) {
            System.out.println("消息为空");
            return isOK;
        }

        rabbitTemplate.convertAndSend(ConstantDef.MY_WORKER_QUEUE, message);

        isOK = true;

        System.out.println(String.format("WorkProducerB发送字符串消息结果：%s", isOK));

        return isOK;
    }
}
