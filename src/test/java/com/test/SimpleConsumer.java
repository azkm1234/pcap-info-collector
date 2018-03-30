package com.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

public class SimpleConsumer {
       private final ConsumerConnector consumer;
       private final String topic;
       public SimpleConsumer(String zookeeper, String groupId, String topic) {
             Properties props = new Properties();
             props.setProperty("zookeeper.connect", zookeeper);
             props.setProperty("zookeeper.session.timeout.ms", "500");
             props.setProperty("group.id", groupId);
             props.setProperty("auto.commit.interval.ms", "1000");
             props.setProperty("zookeeper.sync.time.ms", "250");
             consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
             this.topic = topic;
       }
       public void testConsumer() {
             Map<String, Integer> topicCount = new HashMap<String, Integer>();
             //定义订阅topic的数量
             topicCount.put(topic, 1);
             //返回所有topic的Map
             Map<String, List<KafkaStream<byte[], byte[]>>> consumerStreams =
                           consumer.createMessageStreams(topicCount);
             for (String x : consumerStreams.keySet()) {
                    System.err.println(x);
             }
             List<KafkaStream<byte[], byte[]>> streams = consumerStreams.get(topic);
             for (KafkaStream<byte[], byte[]> stream : streams) {
                    ConsumerIterator<byte[], byte[]> iterator = stream.iterator();
                    while (iterator.hasNext()) {
                           System.err.println("Message from Single Topic:: " + new String(iterator.next().message()));
                    }
             }
             if (consumer != null) {
                    consumer.shutdown();
             }
       }
       public static void main(String[] args) {
             String topic = "mytopic";
             SimpleConsumer consumer = new SimpleConsumer("master:2181,slave1:2181,slave2:2181", "testgroup", topic);
             consumer.testConsumer();
       }
}