package com.htfate.rabbitmq.instance.simpe_queue;

import com.htfate.rabbitmq.entity.vo.GoodsVO;
import com.htfate.rabbitmq.ConstantDef;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class HelloSender {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    public boolean send(String message) throws Exception {
        boolean isOK = false;

        if (StringUtils.isEmpty(message)) {
            System.out.println("消息为空");
            return isOK;
        }

        rabbitTemplate.convertAndSend(ConstantDef.HELLO_STRING_QUEUE, message);

        isOK = true;

        System.out.println(String.format("HelloSender发送字符串消息结果：%s", isOK));

        return isOK;
    }

    public boolean send(GoodsVO goodsVO) throws Exception {

        boolean isOK = false;

        rabbitTemplate.convertAndSend(ConstantDef.HELLO_GOODS_QUEUE, goodsVO);

        isOK = true;

        System.out.println(String.format("HelloSender发送对象消息结果：%s", isOK));

        return isOK;

    }
}
