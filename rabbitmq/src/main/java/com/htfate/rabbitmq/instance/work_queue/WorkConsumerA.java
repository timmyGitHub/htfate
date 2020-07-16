package com.htfate.rabbitmq.instance.work_queue;

import com.htfate.rabbitmq.ConstantDef;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class WorkConsumerA {
    private static AtomicInteger atomicInteger = new AtomicInteger();

    @RabbitListener(queues = ConstantDef.MY_WORKER_QUEUE)
    @RabbitHandler
    public void process(String message) throws Exception {

        int index = atomicInteger.getAndIncrement();

        System.out.println("WorkConsumerA接收到的字符串消息是 => " + message);

        System.out.println("WorkConsumerA自增序号 => " + index);
    }
}
