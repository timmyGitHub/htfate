package com.htfate.rabbitmq.instance.work_queue;

import com.htfate.rabbitmq.ConstantDef;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class WorkConsumerB {
    private static AtomicInteger atomicInteger = new AtomicInteger();

    @RabbitListener(queues = ConstantDef.MY_WORKER_QUEUE)
    @RabbitHandler
    public void process(String message) throws Exception {

        int index = atomicInteger.getAndIncrement();

        Thread.sleep(10);

        System.out.println("WorkConsumerB接收到的字符串消息是 => " + message);

        System.out.println("WorkConsumerB自增序号 => " + index);
    }
}
