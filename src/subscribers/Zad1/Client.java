package subscribers.Zad1;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;
import java.rmi.UnknownHostException;
import java.util.Set;
import java.util.function.Consumer;

import org.junit.runner.Request;

/**
 * Client functionalities.
 *
 */

public abstract class Client {
	protected InetSocketAddress serverAddress;
	protected String username;
	protected SocketChannel channel;
	protected Consumer<String> errorMessagePlace = System.err::println;

	public Client(String username, InetSocketAddress serverAddress) {
		if (username == null || serverAddress == null)
			throw new NullPointerException();
		this.serverAddress = serverAddress;
		this.username = username;
	}

	/**
	 * 
	 * @return false when failed to connect
	 */
	public boolean connect() {
		boolean connected = false;
		try {
			channel = SocketChannel.open();
			channel.configureBlocking(false);
			channel.connect(serverAddress);
			while (!channel.finishConnect()) {
			}
			connected = true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (ConnectException e) {
			errorMessagePlace.accept("can't connect to the server");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connected;
	}

	public void sendRequest(String message) throws IOException {
		ByteBuffer buf = ByteBuffer.allocate(message.length() * 2);
		CharBuffer chbuf = buf.asCharBuffer();
		chbuf.append(message);
		chbuf.flip();
		buf.limit(chbuf.limit() * 2);
		channel.write(buf);
	}

	public String received(long milis) throws IOException {
		StringBuilder bdr = new StringBuilder();
		ByteBuffer buf = ByteBuffer.allocate(200);
		long start = System.currentTimeMillis();

		while (start + milis > System.currentTimeMillis() && channel.read(buf) >= 0) {
			buf.flip();
			bdr.append(buf.asCharBuffer().toString());
			buf.clear();
		}
		return bdr.toString();
	}

	public String received() throws IOException {
		return received(5000l);
	}

	/**
	 * Sets a place to throw error messages.
	 * 
	 * @param cons consumer that takes error message and places it somewhere
	 */
	public void setErrorConsumer(Consumer<String> cons) {
		this.errorMessagePlace = cons;
	}
	public void setUserName(String username) {
		this.username= username;
	}
	public Set<Topic> fetchAllTopics() throws IOException, ClosedChannelException, Exception {
		return simpleRequest(Requests.FETCHTOPIC).justTopics();
	}

	public CliReceiver simpleRequest(Requests req) throws IOException {
		Message ms = new Message(req, this.username);
		connect();

		this.sendRequest(ms.getMessage());
		final String rec = received();
		CliReceiver cliReceiver = new CliReceiver(rec);
		return cliReceiver;
	}
}
