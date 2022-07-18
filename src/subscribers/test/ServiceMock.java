package subscribers.test;

import java.net.InetSocketAddress;

import subscribers.Zad1.Server;

public class ServiceMock extends Thread {
	ServerMock server;

	public ServiceMock(ServerMock server) {
		this.server = server;
	}
	
	public void run() {
		server.listen();
	}
}
