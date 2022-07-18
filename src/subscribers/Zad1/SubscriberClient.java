package subscribers.Zad1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.Set;

/**
 * Client that will fetch news from the server.
 * 
 * @author macie
 *
 */
public class SubscriberClient extends Client {

	public SubscriberClient(String username, InetSocketAddress serverAddress) {
		super(username, serverAddress);
	}

	public static void main(String[] args) {

	}

	public Response subscribeTopics(Set<Topic> topics) throws IOException {
		Message ms = new Message(Requests.SUB, this.username);
		connect();
		ms.addTransDatas(new LinkedList<TransitibleData>(topics));
		this.sendRequest(ms.getMessage());
		CliReceiver cliReceiver = new CliReceiver(received());
		return new Response(cliReceiver.getStatus());
	}

	public CliReceiver unsubscribeMe() throws IOException {
		return this.simpleRequest(Requests.UNSUB);
	}

	public CliReceiver fetchNews() throws IOException {
		CliReceiver receiver = this.simpleRequest(Requests.FETCHINFO);
		return receiver;
	}

}
