package service;

import java.util.LinkedList;
import java.util.Properties;

import javax.annotation.PreDestroy;

import org.springframework.stereotype.Service;

import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig;

@Service(value = "producerPool")
public class ProducerPool {
	 private final int capacity;
	 private final Properties props = new Properties();
	 private LinkedList<Producer<Integer, String>> free;
	 private LinkedList<Producer<Integer, String>> busy;
	 public ProducerPool() {
		 this.free = new LinkedList<Producer<Integer,String>>();
		 this.busy = new LinkedList<Producer<Integer,String>>();
		 this.capacity = 5;
		 for (int i = 0; i < this.capacity; i++) {
			 props.put("metadata.broker.list", "master:9092,slave1:9092,slave2:9092");
	         props.setProperty("serializer.class", "kafka.serializer.StringEncoder");
	         Producer<Integer, String> producer = new Producer<Integer, String>(new ProducerConfig(props));
	         free.add(producer);
		 }
	 }
	 public synchronized Producer<Integer, String> getProducer() throws InterruptedException{
	     if (this.free.size() >= 0) {
	    	 Producer<Integer, String> p = this.free.pop();
	    	 this.busy.add(p);
	    	 return p;
	     } else {
	    	 Thread.sleep(10);
	    	 return getProducer();
	     }
	 }
	 public synchronized void close(Producer<Integer, String> producer) {
		 this.free.add(producer);
		 this.busy.remove(producer);
	 }
	 @PreDestroy
	 public void destory() {
		 for (Producer<Integer, String> p : this.free) {
			 p.close();
		 }
		 for (Producer<Integer, String> p : this.busy) {
			 p.close();
		 }
	 }
}
