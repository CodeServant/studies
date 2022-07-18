package dicserver;

import java.io.IOException;
import java.net.InetSocketAddress;

import dicserver.mainServer.*;
import dicserver.protocol.QueryException;

public class Server {

	public static void main(String[] args) {
		System.out.println("server");
		MainServer server = new MainServer();
		server.addLangServer("en", new InetSocketAddress("localhost", 51152));
		server.addLangServer("de", new InetSocketAddress("localhost", 51155));
		try {

			server.listen(51153);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (QueryException e) {
			e.printStackTrace();
		}
	}
}
