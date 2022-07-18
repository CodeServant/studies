package subscribers.Zad1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;

import org.junit.runner.Request;
/**
 * Client that will manage the data on the server.
 * 
 */
public class AdminClient extends Client {

	public AdminClient(String username, InetSocketAddress serverAddress) {
		super(username, serverAddress);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Sends request to the server to create new topic.
	 * @param topic to create
	 * @return
	 * @throws IOException 
	 */
	public CliReceiver newTopic(Topic topic) throws IOException{
		return topicRequest(topic, Requests.ADDTOPIC);
	}
	
	/**
	 * Sends request to the server to delete topic.
	 * @param topic to delete
	 * @return
	 * @throws IOException 
	 */
	public CliReceiver delTopic(Topic topic) throws IOException {
		return topicRequest(topic, Requests.DELTOPIC);
	}
	
	private CliReceiver topicRequest(Topic topic, Requests req) throws IOException {
		connect();
		Message ms = new Message(req, username);
		ms.addTransData(topic);
		this.sendRequest(ms.getMessage());
		return new CliReceiver(received());
	}
	/**
	 * Sends request to add new Information.
	 * @param info
	 * @return
	 * @throws IOException 
	 */
	public CliReceiver newInfo(Information info) throws IOException {
		connect();
		Message ms = new Message(Requests.NEWINFO, username);
		ms.addTransData(info);
		ms.addTransDatas(new LinkedList<TransitibleData>(info.getTopics()));
		this.sendRequest(ms.getMessage());
		return new CliReceiver(received());
	}
	
}
