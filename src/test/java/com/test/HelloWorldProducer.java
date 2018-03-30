package com.test;

import java.util.Date;
import java.util.Properties;
import java.util.Random;

import kafka.producer.KeyedMessage;
import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig;

public class HelloWorldProducer {
	public static void main(String[] args) {
        long events = 10;
        Random rnd = new Random();
 
        Properties props = new Properties();
        //����kafka��Ⱥ��broker��ַ�����������������ϣ���������һ��ʧЧ��������Ҫ��ȫ����Ⱥ���Զ�����leader�ڵ㡣
        props.put("metadata.broker.list", "master:9092,slave1:9092,slave2:9092");
        //����value�����л���
        //key�����л���key.serializer.class���Ե������ã�Ĭ��ʹ��value�����л���
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        //����partitionnerѡ����ԣ���ѡ����
        props.put("partitioner.class", "cn.ljh.kafka.kafka_helloworld.SimplePartitioner");
        props.put("request.required.acks", "1");
 
        ProducerConfig config = new ProducerConfig(props);
 
        Producer<String, String> producer = new Producer<String, String>(config);
 
        for (long nEvents = 0; nEvents < events; nEvents++) { 
               long runtime = new Date().getTime();  
               String ip = "192.168.2." + rnd.nextInt(255); 
               String msg = runtime + ",www.example.com," + ip; 
               KeyedMessage<String, String> data = new KeyedMessage<String, String>("page_visits", ip, msg);
               producer.send(data);
        }
        producer.close();
    }
}
