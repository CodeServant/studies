package subscribers.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.junit.jupiter.api.Test;

import subscribers.Zad1.*;

class SrvCliTest {

	@Test
	void testConnection() {
		try {

			ServiceMock server = startServer();
			SubscriberClientMock client = getDefaultSubCli();
			
			testResponses(client, "ok;;", "okresp;;");
			testResponses(client, "{sub;john}{edukacja;testingTopic1};;", "{1};;");
			testResponses(client, "{fetchinfo;john};;", "{1}{{2022-02-01 00:00:02;Testing informationfirst}};;");
			testResponses(client, "{fetchinfo;john};;", "{3};;");
			Thread.sleep(1000);
			Information info = new Information("Testing information secound");
			Message ms = new Message(Requests.NEWINFO, "john");
			ms.addTransData(info);

			testResponses(client, "{newinfo;john}{" + info.toSentMessage() + ";testingTopic1;testingTopic3};;",
					"{1};;");
			// cant see newly added info, look previous assertion
			testResponses(client, "{fetchinfo;john};;", "{1}{" + info.toSentMessage() + "};;");
			testResponses(client, "{unsub;john};;", "{1};;");
			testResponses(client, "{fetchinfo;john};;", "{4};;");// authentication error
			testResponses(client, "{sub;john}{edukacja;testingTopic1};;", "{1};;");
			testResponses(client, "{deltopic;john}{testingTopic1};;", "{1};;");
			//deleting subscribed topic
			testResponses(client, "{fetchinfo;john};;", "{3};;");
			testResponses(client, "{fetchtopic;john};;", "{1}{testingTopic3};;");
			testResponses(client, "{addtopic;john}{added_topic1};;", "{1};;");
			//topics added
			testResponses(client, "{fetchtopic;john};;", "{1}{added_topic1;testingTopic3};;");
			assertEquals("[added_topic1, testingTopic3]", client.fetchAllTopics().toString());
			
			
			server.interrupt();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void testResponses(Client client, String request, String expectedResult) {
		connect(client);
		try {
			client.sendRequest(request);
			assertEquals(expectedResult, client.received());
		} catch (IOException e) {
			e.printStackTrace();
		} 

	}

	public ServerMock getDefaultServer() {
		return new ServerMock(new InetSocketAddress("localhost", 50001));
	}

	public SubscriberClientMock getDefaultSubCli() {
		InetSocketAddress serverAddress = new InetSocketAddress("localhost", 50001);
		SubscriberClientMock client = new SubscriberClientMock("john", serverAddress);
		return client;
	}

	private ServiceMock startServer() {
		ServiceMock server = new ServiceMock(this.getDefaultServer());
		server.start();
		try {
			Thread.sleep(50);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		return server;
	}

	private void connect(Client client) {
		if (!client.connect())
			fail();
	}
}
