package com.htfate.rabbitmq.instance.simpe_queue;

import com.htfate.rabbitmq.entity.vo.GoodsVO;
import com.htfate.rabbitmq.ConstantDef;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class HelloReceiver {
    @RabbitListener(queues = ConstantDef.HELLO_STRING_QUEUE)
    @RabbitHandler
    public void process(String message) {

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("HelloReceiver接收到的字符串消息是 => " + message);
    }


    @RabbitListener(queues = ConstantDef.HELLO_GOODS_QUEUE)
    @RabbitHandler
    public void process(GoodsVO goodsVO) {
        System.out.println("------ 接收实体对象 ------");
//        System.out.println("HelloReceiver接收到的实体对象是 => " + SerializeUtil.Serialize(goodsVO));
        System.out.println("HelloReceiver接收到的实体对象是 => " + goodsVO);
    }
}
