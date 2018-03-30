package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import iservice.IKafKaDataSender;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
@Service
public class KafKaDataSender implements IKafKaDataSender {
	@Autowired
	private ProducerPool producerPool;
	public void send(String message, String topic) {
		new Thread(new SendTask(producerPool, message, topic)).start();
	}
}
class SendTask implements Runnable {
	public String topic;
	private ProducerPool pool;
	private String msg;
	public SendTask(ProducerPool producerPool, String message, String topic) {
		this.pool = producerPool;
		this.msg = message;
		this.topic = topic;
	}
	public void run() {
		Producer<Integer, String> producer;
		try {
			producer = this.pool.getProducer();
			KeyedMessage<Integer, String> data = new KeyedMessage<Integer, String>(topic, this.msg);
	        producer.send(data);
	        this.pool.close(producer);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.err.println("*******getProducer Error*********");
		}
		
	}
}
