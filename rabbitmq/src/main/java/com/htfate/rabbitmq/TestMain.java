package com.htfate.rabbitmq;

public class TestMain {
    public static void main(String[] args) {
        String routingKey = "my topic routingkeya.16";
        String str = routingKey.split("\\.")[0];
        boolean b = ConstantDef.MY_TOPIC_ROUTINGKEYA.contains(str);
        System.out.println(str);
        System.out.println(ConstantDef.MY_TOPIC_ROUTINGKEYA);
        System.out.println(b);
    }
}
