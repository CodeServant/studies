package subscribers.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.*;
import subscribers.Zad1.*;

public class ServerMock extends Server {

	public ServerMock(InetSocketAddress address) {
		super(address);
		initSubscribers();
		initInfo();
	}

	public void whileRead(SelectionKey key) throws IOException {
		super.whileRead(key);
	}

	public void whileWrite(SelectionKey key) throws IOException {
		super.whileWrite(key);
	}
	protected String respond(Session session) {
		final String ms = session.getReceivedMessage();
		if(ms.equals("ok;;")) return "okresp;;";
		else
			return super.respond(session);
	}
	protected void initSubscribers() {
		Set<Topic> tps = new TreeSet<>();
		tps.add(new Topic("testingTopic1"));
		tps.add(new Topic("testingTopic2"));
		this.addSubscriber(new Subscriber("testUser", tps));
	}
	protected void initInfo() {
		InformationMock info = new InformationMock("Testing informationfirst");
		info.getTopics().add(new Topic("testingTopic1"));
		info.setDate(2000);
		try {
			Information.addInformation(info);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
