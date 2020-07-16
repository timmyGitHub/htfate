package com.htfate.rabbitmq.instance.pubsub.fanout;

import com.htfate.rabbitmq.ConstantDef;
import com.htfate.rabbitmq.entity.vo.GoodsVO;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class FanoutReceiverA {
    @RabbitListener(queues = ConstantDef.MY_FANOUTA_QUEUE)
    @RabbitHandler
    public void process(GoodsVO goodsVO) {
        System.out.println("FanoutReceiverA接收到的商品消息是 => " + goodsVO);
    }
}
