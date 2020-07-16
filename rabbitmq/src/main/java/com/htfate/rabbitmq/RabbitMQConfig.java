package com.htfate.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {


    /* -----------------------------------------------------------这是一个简单的队列----------------------------------- */

    /**
     * 声明接收字符串的队列 Hello 默认
     *
     * @return
     */
    @Bean
    public Queue stringQueue() {

        //boolean isDurable = true;//是否持久化
        //boolean isExclusive = false;  //仅创建者可以使用的私有队列，断开后自动删除
        //boolean isAutoDelete = false;  //当所有消费客户端连接断开后，是否自动删除队列
        //Queue queue = new Queue(ConstantDef.HELLO_STRING_QUEUE, isDurable, isExclusive, isAutoDelete);
        //return  queue;

        //return new Queue(ConstantDef.HELLO_STRING_QUEUE); //默认支持持久化

        return QueueBuilder.durable(ConstantDef.HELLO_STRING_QUEUE)
                //.exclusive()
                //.autoDelete()
                .build();
    }

    /**
     * 声明接收Goods对象的队列 Hello  支持持久化
     *
     * @return x
     */
    @Bean
    public Queue goodsQueue() {

        return QueueBuilder.durable(ConstantDef.HELLO_GOODS_QUEUE).build();
    }

    /*---------------------------------------------这是一个work-queue的队列---------------------------------------*/

    /**
     * 声明WorkQueue队列 competing consumers pattern，多个消费者不会重复消费队列的相同消息
     *
     * @return x
     */
    @Bean
    public Queue workQueue() {
        return QueueBuilder.durable(ConstantDef.MY_WORKER_QUEUE).build();
    }

    /*---------------------------------------------这是一个pubsub-queue的队列---------------------------------------*/

    /**
     * s
     * 消息队列中最常见的模式：发布订阅模式
     * <p>
     * 声明发布订阅模式队列 Publish/Subscribe
     * <p>
     * exchange类型包括：direct, topic, headers 和 fanout
     **/

    /*fanout（广播）队列相关声明开始*/
    @Bean
    public Queue fanOutAQueue() {
        return QueueBuilder.durable(ConstantDef.MY_FANOUTA_QUEUE).build();
    }
    @Bean
    public Queue fanOutBQueue() {
        return QueueBuilder.durable(ConstantDef.MY_FANOUTB_QUEUE).build();
    }
    // 申明一个交换机
    @Bean
    FanoutExchange fanoutExchange() {
        return (FanoutExchange) ExchangeBuilder.fanoutExchange(ConstantDef.MY_FANOUT_EXCHANGE).build();

        //return new FanoutExchange(ConstantDef.MY_FANOUT_EXCHANGE);
    }
    // 绑定交换机
    @Bean
    Binding bindingExchangeA(Queue fanOutAQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanOutAQueue).to(fanoutExchange);
    }
    @Bean
    Binding bindingExchangeB(Queue fanOutBQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanOutBQueue).to(fanoutExchange);
    }
    /*fanout队列相关声明结束*/


    /*topic队列相关声明开始*/

    @Bean
    public Queue topicAQueue() {
        return QueueBuilder.durable(ConstantDef.MY_TOPICA_QUEUE).build();
    }
    @Bean
    public Queue topicBQueue() {
        return QueueBuilder.durable(ConstantDef.MY_TOPICB_QUEUE).build();
    }
    @Bean
    TopicExchange topicExchange() {
        return (TopicExchange) ExchangeBuilder.topicExchange(ConstantDef.MY_TOPIC_EXCHANGE).build();
    }
    //绑定时，注意队列名称与上述方法名一致
    @Bean
    Binding bindingTopicAExchangeMessage(Queue topicAQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(topicAQueue).to(topicExchange).with(ConstantDef.MY_TOPIC_ROUTINGKEYA);
    }
    @Bean
    Binding bindingTopicBExchangeMessages(Queue topicBQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(topicBQueue).to(topicExchange).with(ConstantDef.MY_TOPIC_ROUTINGKEYB);
    }
    /*topic队列相关声明结束*/

    /*direct队列相关声明开始*/
    @Bean
    public Queue directAQueue() {
        return QueueBuilder.durable(ConstantDef.MY_DIRECTA_QUEUE).build();
    }

    @Bean
    public Queue directBQueue() {
        return QueueBuilder.durable(ConstantDef.MY_DIRECTB_QUEUE).build();
    }

    /**
     * 声明Direct交换机 支持持久化.
     *
     * @return the exchange
     */
    @Bean
    DirectExchange directExchange() {
        return (DirectExchange) ExchangeBuilder.directExchange(ConstantDef.MY_DIRECT_EXCHANGE).durable(true).build();
    }

    @Bean
    Binding bindingDirectAExchangeMessage(Queue directAQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(directAQueue).to(directExchange).with(ConstantDef.MY_DIRECT_ROUTINGKEYA);
    }

    @Bean
    Binding bindingDirectBExchangeMessage(Queue directBQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(directBQueue).to(directExchange)
                //.with(ConstantDef.MY_DIRECT_ROUTINGKEYB)
                .with(ConstantDef.MY_DIRECT_ROUTINGKEYB);
    }
    /*direct队列相关声明结束*/
}
