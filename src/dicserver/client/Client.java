package dicserver.client;

import java.io.*;
import java.net.*;

import dicserver.protocol.*;

/**
 * Creates objects for handling Client site actions.
 *
 */
public class Client {
	public InetSocketAddress serverAddress;

	public Client(InetSocketAddress serverAddress) {
		this.serverAddress = serverAddress;
	}

	/**
	 * Atempt to translate the given word with specific language code.
	 * 
	 * @param word
	 * @param langCode
	 * @return
	 * @throws IOException
	 * @throws QueryException
	 */
	public String get(String word, String langCode) throws IOException, QueryException {
		ServerSocket servSocket = new ServerSocket(0);

		this.sendRequest(word, langCode, servSocket.getLocalPort());
		Socket incomingSocket = servSocket.accept();
		InputStream inStream = incomingSocket.getInputStream();
		ServerResponse response = new ServerResponse(inStream);
		incomingSocket.close();
		String retrieved = response.getTranslated();
		return retrieved;
	}

	/**
	 * Attempt to fetch port number from registered server to connectTo.
	 * 
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public int askPort() throws UnknownHostException, IOException {
		Socket socket2 = new Socket(serverAddress.getHostString(), serverAddress.getPort());
		InputStream inStream = socket2.getInputStream();
		byte[] recieved = new byte[4];
		inStream.read(recieved);
		socket2.close();
		return Client.toInt(recieved);
	}

	public static int toInt(byte[] fourBytes) {
		int result = fourBytes[0];
		for (int i = 1; i < 4; i++) {
			result = (result << 8);
			result |= (fourBytes[i] & 0b11111111);
		}
		return result;
	}

	/**
	 * Sends request to the main server for the specific word to translate.
	 * 
	 * @param word
	 * @param langCode
	 * @param port
	 * @throws IOException
	 * @throws QueryException
	 */
	public void sendRequest(String word, String langCode, int port) throws IOException, QueryException {
		ClientQuery query = new ClientQuery(word, langCode, port);

		int newServerPort = askPort();
		Socket socket = new Socket(serverAddress.getHostString(), newServerPort);
		OutputStream oStream = socket.getOutputStream();
		query.send(oStream);
		socket.close();
	}
}
