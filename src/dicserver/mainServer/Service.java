package dicserver.mainServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.TreeMap;

import dicserver.protocol.*;

public class Service implements Runnable {
	private ServerSocket servSocket;
	protected TreeMap<String, InetSocketAddress> servers;

	public Service(ServerSocket servSocket, TreeMap<String, InetSocketAddress> servers) {
		this.servSocket = servSocket;
		this.servers = servers;
	}

	@Override
	public void run() {
		Socket socket;
		try {
			socket = servSocket.accept();
			ClientQuery query = new ClientQuery(socket.getInputStream());
			this.serve(query, socket.getInetAddress());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (QueryException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Serves the given query to a proper language server.
	 * 
	 * @param query
	 * @param clientAddress
	 * @throws IOException
	 * @throws QueryException
	 */
	private void serve(ClientQuery query, InetAddress clientAddress) throws IOException, QueryException {
		String langCode = query.getLangCode();
		if (servers.keySet().contains(langCode)) {
			try {
				InetSocketAddress serverAddress = this.serverAddress(langCode);
				ServerQuery cliQry = new ServerQuery(query.getWord(), clientAddress, query.getReceivePort());
				Socket socket = new Socket(serverAddress.getAddress(), serverAddress.getPort());
				OutputStream socOut = socket.getOutputStream();
				cliQry.send(socOut);
				socket.close();
			} catch (ConnectException e) {
				ServerResponse response = new ServerResponse("can not connect to language server");
				Socket socket = new Socket(clientAddress.getHostAddress(), query.getReceivePort());
				OutputStream odp = socket.getOutputStream();
				response.send(odp);
				odp.close();
			}

		} else {
			Socket socket = new Socket(clientAddress, query.getReceivePort());
			ServerResponse response = new ServerResponse("no such language server");
			response.send(socket.getOutputStream());
			socket.close();
		}
	}

	/**
	 * Returns server address. When anavailble than null.
	 * 
	 * @param langCode
	 * @return
	 */
	public InetSocketAddress serverAddress(String langCode) {
		return servers.get(langCode);
	}

}
