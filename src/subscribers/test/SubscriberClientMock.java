package subscribers.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Channel;

import subscribers.Zad1.*;

public class SubscriberClientMock extends SubscriberClient {

	public SubscriberClientMock(String username, InetSocketAddress serverAddress) {
		super(username, serverAddress);
	}

	/**
	 * Receiving info after send request.
	 */
	public String print() throws IOException {
		return received();
	}
	/**
	 * Texting server response.
	 * @throws IOException
	 */
	public void send() throws IOException {
		sendRequest("ok;;");
	}

	public Channel channel() {
		return channel;
	}
	
}
