package com.test;

import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class SimpleProducer {
    private static Producer<Integer,String> producer;
    private final Properties props = new Properties();
    public SimpleProducer() {
          props.put("metadata.broker.list", "master:9092,slave1:9092,slave2:9092");
          props.setProperty("serializer.class", "kafka.serializer.StringEncoder");
          producer = new Producer<Integer, String>(new ProducerConfig(props));
    }
    public static void main(String[] args) {
          SimpleProducer sp = new SimpleProducer();
          
          String topic = "mytopic";
          String messageStr = "Hello World, send a message to you";
          
          KeyedMessage<Integer, String> data = new KeyedMessage<Integer, String>(topic, messageStr);
          producer.send(data);
          producer.close();
    }
}
