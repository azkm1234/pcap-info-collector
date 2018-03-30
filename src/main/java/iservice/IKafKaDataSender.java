package iservice;

public interface IKafKaDataSender {
	public void send(String message, String topic);
}
