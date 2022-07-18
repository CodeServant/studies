package dicserver.mainServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.util.TreeMap;

import dicserver.protocol.*;

/**
 * MainServer forwards Clients question to the proper language server.
 *
 */
public class MainServer {
	protected TreeMap<String, InetSocketAddress> servers;

	public MainServer() {
		this.servers = new TreeMap<>();
	}

	public MainServer(TreeMap<String, InetSocketAddress> servers) {
		this.servers = servers;
	}

	/**
	 * This method changes integer to 4 bytes.
	 * 
	 * @param integer
	 * @return
	 */
	public static byte[] toBytes(int integer) {
		byte[] result = new byte[4];

		for (int i = 3; i >= 0; i--) {
			result[i] = (byte) (0b11111111 & integer);
			integer = integer >>> 8;
		}
		return result;
	}

	/**
	 * Adds new language servers to database of servers.
	 * 
	 * @param k
	 * @param v
	 */
	public void addLangServer(String k, InetSocketAddress v) {
		this.servers.put(k, v);
	}

	/**
	 * Listent to and opens a service to the incoming connection.
	 * 
	 * @param port port to listen to
	 * @throws IOException
	 * @throws QueryException
	 */
	public void listen(int port) throws IOException, QueryException {
		ServerSocket servSocket = new ServerSocket(port);
		while (true) {
			Socket smSocket = servSocket.accept();
			// sending new port number to the client
			OutputStream outStream = smSocket.getOutputStream();
			ServerSocket newServerSocket = new ServerSocket(0);
			byte[] portMessage = MainServer.toBytes(newServerSocket.getLocalPort());
			outStream.write(portMessage);
			outStream.close();
			// serve client
			this.serveClient(newServerSocket);
		}
	}

	public void serveClient(ServerSocket servSocket) throws IOException, QueryException {
		Service service = new Service(servSocket, this.servers);
		Thread serving = new Thread(service);
		serving.start();
	}
}
