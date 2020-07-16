package com.htfate.rabbitmq.instance.pubsub.fanout;

import com.htfate.rabbitmq.entity.vo.GoodsVO;
import com.htfate.rabbitmq.ConstantDef;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FanoutSender {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    public boolean send(GoodsVO goodsVO) {

        boolean isOK = false;

        if (goodsVO == null) {
            System.out.println("消息为空");
            return isOK;
        }

        rabbitTemplate.convertAndSend(ConstantDef.MY_FANOUT_EXCHANGE, "", goodsVO);

        isOK = true;

        System.out.println(String.format("FanoutSender发送对象消息结果：%s", isOK));

        return isOK;

    }
}
